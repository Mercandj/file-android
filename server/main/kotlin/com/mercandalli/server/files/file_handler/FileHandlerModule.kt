package com.mercandalli.server.files.file_handler

import com.mercandalli.server.files.main.ApplicationGraph

class FileHandlerModule {

    private val logManager by lazy { ApplicationGraph.getLogManager() }
    private val fileRepository by lazy { ApplicationGraph.getFileRepository() }

    fun createFileHandlerGet(): FileHandlerGet {
        return FileHandlerGetImpl(
                fileRepository,
                logManager
        )
    }

    fun createFileHandlerPost(): FileHandlerPost {
        return FileHandlerPostImpl(
                fileRepository,
                logManager
        )
    }

    fun createFileHandlerDelete(): FileHandlerDelete {
        return FileHandlerDeleteImpl(
                fileRepository,
                logManager
        )
    }
}