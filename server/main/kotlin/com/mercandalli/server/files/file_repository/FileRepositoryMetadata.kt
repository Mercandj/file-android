package com.mercandalli.server.files.file_repository

import com.mercandalli.sdk.files.api.File
import org.json.JSONObject

data class FileRepositoryMetadata(

        private val files: HashMap<String, File>
) {

    fun getFiles(): HashMap<String, File> {
        return HashMap(files)
    }

    companion object {

        fun fromJson(jsonObject: JSONObject): FileRepositoryMetadata {
            val filesJsonArray = jsonObject.getJSONArray("files")
            val filesList = File.fromJson(filesJsonArray)
            val files = HashMap<String, File>()
            for (file in filesList) {
                files[file.path] = file
            }
            return FileRepositoryMetadata(
                    files
            )
        }

        fun toJson(fileRepositoryMetadata: FileRepositoryMetadata): JSONObject {
            val json = JSONObject()
            val values = fileRepositoryMetadata.files.values
            val filesJsonArray = File.toJson(values)
            json.put("files", filesJsonArray)
            return json
        }

        fun createEmptyJson(): String {
            val files = HashMap<String, File>()
            val fileRepositoryMetadata = FileRepositoryMetadata(files)
            return toJson(fileRepositoryMetadata).toString()
        }

        fun put(fileRepositoryMetadata: FileRepositoryMetadata, file: File): FileRepositoryMetadata {
            val files = fileRepositoryMetadata.getFiles()
            files[file.path] = file
            return FileRepositoryMetadata(files)
        }

        fun delete(fileRepositoryMetadata: FileRepositoryMetadata, path: String): FileRepositoryMetadata {
            val files = fileRepositoryMetadata.getFiles()
            files.remove(path)
            return FileRepositoryMetadata(files)
        }

        fun rename(fileRepositoryMetadata: FileRepositoryMetadata, path: String, name: String): FileRepositoryMetadata {
            val files = fileRepositoryMetadata.getFiles()
            val file = files[path]!!
            val renamedFile = File.rename(file, name)
            files.remove(path)
            files[renamedFile.path] = renamedFile
            val filesToAdd = ArrayList<File>()
            val pathsToRemove = ArrayList<String>()
            if (file.directory) {
                for (currentPath in files.keys) {
                    if (currentPath.startsWith(path)) {
                        val newParentPath = "$path/${currentPath.replaceFirst(path, "")}"
                        val currentFile = files[currentPath]!!
                        val currentRenamedFile = File.setParent(currentFile, newParentPath)
                        pathsToRemove.add(currentPath)
                        filesToAdd.add(currentRenamedFile)
                    }
                }
            }
            for (fileToAdd in filesToAdd) {
                files[file.path] = fileToAdd
            }
            for (pathToRemove in pathsToRemove) {
                files.remove(pathToRemove)
            }
            return FileRepositoryMetadata(files)
        }

        fun copy(fileRepositoryMetadata: FileRepositoryMetadata, path: String, pathDirectoryOutput: String): FileRepositoryMetadata {
            val files = fileRepositoryMetadata.getFiles()
            val file = files[path]!!
            val renamedFile = File.setParent(file, pathDirectoryOutput)
            files[renamedFile.path] = renamedFile
            return FileRepositoryMetadata(files)
        }

        fun cut(fileRepositoryMetadata: FileRepositoryMetadata, path: String, pathDirectoryOutput: String): FileRepositoryMetadata {
            val files = fileRepositoryMetadata.getFiles()
            val file = files[path]!!
            val renamedFile = File.setParent(file, pathDirectoryOutput)
            files.remove(path)
            files[renamedFile.path] = renamedFile
            return FileRepositoryMetadata(files)
        }
    }
}