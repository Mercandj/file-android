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

    override fun has(id: String) = fileRepositoryMetadata.files.containsKey(id)

    override fun get(id: String): File {
        return fileRepositoryMetadata.files[id]!!
    }

    override fun get(): List<File> {
        val values = fileRepositoryMetadata.files.values
        return ArrayList<File>(values)
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
