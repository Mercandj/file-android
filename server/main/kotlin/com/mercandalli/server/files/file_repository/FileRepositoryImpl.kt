package com.mercandalli.server.files.file_repository

import com.mercandalli.sdk.files.api.File
import org.json.JSONObject

class FileRepositoryImpl(
        private val fileMetadataJson: java.io.File,
        private val folderContainer: java.io.File
) : FileRepository {

    private var fileRepositoryMetadata = FileRepositoryMetadata(
            HashMap()
    )

    init {
        load()
    }

    override fun getFolderContainerPath() = folderContainer.absolutePath!!

    override fun has(path: String) = fileRepositoryMetadata.getFiles().containsKey(path)

    override fun get(): List<File> {
        val files = fileRepositoryMetadata.getFiles().values
        return ArrayList<File>(files)
    }

    override fun get(path: String): File {
        return fileRepositoryMetadata.getFiles()[path]!!
    }

    override fun getFromParent(parentPath: String): List<File> {
        val files = fileRepositoryMetadata.getFiles().values
        return files.filter {
            it.parentPath == parentPath
        }
    }

    override fun put(file: File) {
        fileRepositoryMetadata = FileRepositoryMetadata.put(fileRepositoryMetadata, file)
        save()
    }

    override fun delete(path: String): File? {
        if (!has(path)) {
            return null
        }
        val file = get(path)
        fileRepositoryMetadata = FileRepositoryMetadata.delete(fileRepositoryMetadata, path)
        save()
        return file
    }

    override fun rename(path: String, name: String): File? {
        if (!has(path)) {
            return null
        }
        fileRepositoryMetadata = FileRepositoryMetadata.rename(fileRepositoryMetadata, path, name)
        save()
        return get(File.renamePathFromName(path, name))
    }

    override fun copy(path: String, pathOutput: String): File? {
        if (!has(path)) {
            return null
        }
        fileRepositoryMetadata = FileRepositoryMetadata.copy(fileRepositoryMetadata, path, pathOutput)
        save()
        return get(pathOutput)
    }

    override fun cut(path: String, pathOutput: String): File? {
        if (!has(path)) {
            return null
        }
        fileRepositoryMetadata = FileRepositoryMetadata.cut(fileRepositoryMetadata, path, pathOutput)
        save()
        return get(pathOutput)
    }

    private fun load() {
        val json = fileMetadataJson.readText()
        fileRepositoryMetadata = FileRepositoryMetadata.fromJson(JSONObject(json))
    }

    private fun save() {
        val json = FileRepositoryMetadata.toJson(fileRepositoryMetadata)
        fileMetadataJson.writeText(json.toString())
    }
}
