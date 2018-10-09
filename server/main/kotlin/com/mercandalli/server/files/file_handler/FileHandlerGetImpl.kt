package com.mercandalli.server.files.file_handler

import com.mercandalli.sdk.files.api.File
import com.mercandalli.server.files.file_repository.FileRepository
import com.mercandalli.server.files.log.LogManager
import com.mercandalli.server.files.response_json.ResponseJsonObject
import org.json.JSONObject

class FileHandlerGetImpl(
        private val fileRepository: FileRepository,
        private val logManager: LogManager
) : FileHandlerGet {

    override fun get(): String {
        logManager.d(TAG, "get()")
        val files = fileRepository.get()
        val filesJsonArray = File.toJson(files)
        val json = JSONObject()
        json.put("files", filesJsonArray)
        return ResponseJsonObject(
                json,
                "Get all the files"
        ).toJsonString()
    }

    override fun get(id: String): String {
        logManager.d(TAG, "get(id: $id)")
        val file = File.createFake(id)
        val fileJson = File.toJson(file)
        val json = JSONObject()
        json.put("file", fileJson)
        return ResponseJsonObject(
                json,
                "Get one file"
        ).toJsonString()
    }

    companion object {
        private const val TAG = "FileHandlerGet"
    }
}