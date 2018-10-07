package com.mercandalli.server.files.file

import com.mercandalli.server.files.main.ApplicationGraph

class FileModule {

    fun createFileGetHandler(): FileGetHandler {
        val logManager = ApplicationGraph.getLogManager()
        return FileGetHandlerImpl(
                logManager
        )
    }
}