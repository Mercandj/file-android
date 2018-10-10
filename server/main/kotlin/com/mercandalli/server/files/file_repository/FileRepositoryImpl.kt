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

    override fun put(file: File) {
        fileRepositoryMetadata.files[file.id] = file
        save()
    }

    override fun has(path: String) = fileRepositoryMetadata.files.containsKey(path)

    override fun get(path: String): File {
        return fileRepositoryMetadata.files[path]!!
    }

    override fun get(): List<File> {
        val values = fileRepositoryMetadata.files.values
        return ArrayList<File>(values)
    }

    override fun delete(path: String): Boolean {
        if (!has(path)) {
            return false
        }
        fileRepositoryMetadata.files.remove(path)
        save()
        return true
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
