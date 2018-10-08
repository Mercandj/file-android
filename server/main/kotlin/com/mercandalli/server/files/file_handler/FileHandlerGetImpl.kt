package com.mercandalli.server.files.file_handler

import com.mercandalli.sdk.files.api.File
import com.mercandalli.server.files.file_repository.FileRepository
import com.mercandalli.server.files.log.LogManager

class FileHandlerGetImpl(
        private val fileRepository: FileRepository,
        private val logManager: LogManager
) : FileHandlerGet {

    override fun get(): String {
        logManager.d(TAG, "get()")
        val files = fileRepository.get()
        val filesJsonArray = File.toJson(files)
        return filesJsonArray.toString()
    }

    override fun get(id: String): String {
        logManager.d(TAG, "get(id: $id)")
        val file = File.createFake(id)
        val fileJson = File.toJson(file)
        return fileJson.toString()
    }

    companion object {
        private const val TAG = "FileHandlerGet"
    }
}