package com.mercandalli.server.files.file

import com.mercandalli.sdk.files.api.File
import com.mercandalli.server.files.log.LogManager

class FileGetHandlerImpl(
        private val logManager: LogManager
) : FileGetHandler {

    override fun get(id: String): String {
        logManager.log("[FileGetHandlerImpl] get(id: $id)")
        val file = File(
                id,
                "path",
                "parentPath",
                true,
                "RootDir",
                0,
                4242
        )
        return File.toJson(file).toString()
    }
}