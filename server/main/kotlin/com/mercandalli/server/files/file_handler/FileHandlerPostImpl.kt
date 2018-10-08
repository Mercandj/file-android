package com.mercandalli.server.files.file_handler

import com.mercandalli.sdk.files.api.File
import com.mercandalli.server.files.file_repository.FileRepository
import com.mercandalli.server.files.log.LogManager
import org.json.JSONObject

class FileHandlerPostImpl(
        private val fileRepository: FileRepository,
        private val logManager: LogManager
) : FileHandlerPost {

    override fun post(body: String): String {
        logManager.d(TAG, "post(body: $body)")
        val fileJsonObject = JSONObject(body)
        val file = File.fromJson(fileJsonObject)
        fileRepository.put(file)
        val response = JSONObject()
        response.put("file", fileJsonObject)
        response.put("debug", "File inserted into the repository")
        return response.toString()
    }

    companion object {
        private const val TAG = "FileHandlerPost"
    }
}