package com.mercandalli.server.files.file_handler

import com.mercandalli.sdk.files.api.File
import com.mercandalli.server.files.file_repository.FileRepository
import com.mercandalli.server.files.log.LogManager
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFile
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFiles

class FileHandlerGetImpl(
        private val fileRepository: FileRepository,
        private val logManager: LogManager
) : FileHandlerGet {

    override fun get(): String {
        logManager.d(TAG, "get()")
        val files = fileRepository.get()
        return ServerResponseFiles.create(
                files,
                "Get all the files"
        ).toJsonString()
    }

    override fun get(id: String): String {
        logManager.d(TAG, "get(id: $id)")
        val file = File.createFake(id)
        return ServerResponseFile.create(
                file,
                "Get one file"
        ).toJsonString()
    }

    companion object {
        private const val TAG = "FileHandlerGet"
    }
}