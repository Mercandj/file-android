@file:Suppress("PackageName")

/* ktlint-disable package-name */
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

    override fun getChildren(parentPath: String): List<File> {
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

    override fun copy(path: String, pathDirectoryOutput: String): File? {
        if (!has(path)) {
            return null
        }
        fileRepositoryMetadata = FileRepositoryMetadata.copy(fileRepositoryMetadata, path, pathDirectoryOutput)
        save()
        val name = java.io.File(path).name
        val copiedPath = java.io.File(pathDirectoryOutput, name).absolutePath
        return get(copiedPath)
    }

    override fun cut(path: String, pathDirectoryOutput: String): File? {
        if (!has(path)) {
            return null
        }
        fileRepositoryMetadata = FileRepositoryMetadata.cut(fileRepositoryMetadata, path, pathDirectoryOutput)
        save()
        val name = java.io.File(path).name
        val cutPath = java.io.File(pathDirectoryOutput, name).absolutePath
        return get(cutPath)
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
