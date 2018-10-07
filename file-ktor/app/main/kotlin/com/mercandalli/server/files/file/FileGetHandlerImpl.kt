package com.mercandalli.server.files.file

import com.mercandalli.sdk.files.api.File
import com.mercandalli.server.files.log.LogManager

class FileGetHandlerImpl(
        private val logManager: LogManager
) : FileGetHandler {

    override fun get(): String {
        logManager.d(TAG, "get()")
        val files = ArrayList<File>()
        val file = createFake()
        files.add(file)
        val filesJsonArray = File.toJson(files)
        return filesJsonArray.toString()
    }

    override fun post(): String {
        logManager.d(TAG, "post()")
        return "{}"
    }

    override fun get(id: String): String {
        logManager.d(TAG, "get(id: $id)")
        val file = createFake(id)
        val fileJson = File.toJson(file)
        return fileJson.toString()
    }

    private fun createFake(): File {
        return File(
                "id",
                "path",
                "parentPath",
                true,
                "RootDir",
                0,
                4242
        )
    }

    private fun createFake(id: String): File {
        return File(
                id,
                "path",
                "parentPath",
                true,
                "RootDir",
                0,
                4242
        )
    }

    companion object {
        private const val TAG = "FileGetHandler"
    }
}