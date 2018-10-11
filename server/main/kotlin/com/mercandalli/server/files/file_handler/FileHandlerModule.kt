package com.mercandalli.server.files.file_handler

import com.mercandalli.server.files.main.ApplicationGraph

class FileHandlerModule {

    private val logManager by lazy { ApplicationGraph.getLogManager() }
    private val fileRepository by lazy { ApplicationGraph.getFileRepository() }
    private val fileOnlineAuthentications by lazy { ApplicationGraph.getFileOnlineAuthentications() }

    fun createFileHandlerGet(): FileHandlerGet {
        return FileHandlerGetImpl(
                fileRepository,
                logManager
        )
    }

    fun createFileHandlerPost(): FileHandlerPost {
        return FileHandlerPostImpl(
                fileRepository,
                logManager,
                fileOnlineAuthentications
        )
    }

    fun createFileHandlerDelete(): FileHandlerDelete {
        return FileHandlerDeleteImpl(
                fileRepository,
                logManager
        )
    }
}