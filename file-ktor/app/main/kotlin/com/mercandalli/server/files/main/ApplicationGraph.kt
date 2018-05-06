package com.mercandalli.server.files.main

import com.mercandalli.server.files.file.FileGetHandler
import com.mercandalli.server.files.file.FileGetHandlerImpl
import com.mercandalli.server.files.log.LogManager
import com.mercandalli.server.files.log.LogManagerImpl

class ApplicationGraph {

    private lateinit var logManager: LogManager
    private lateinit var fileGetHandler: FileGetHandler

    private fun getLogManagerInternal(): LogManager {
        if (!::logManager.isInitialized) {
            logManager = LogManagerImpl()
        }
        return logManager
    }

    private fun getFileGetHandlerInternal(): FileGetHandler {
        if (!::fileGetHandler.isInitialized) {
            val logManager = getLogManagerInternal()
            fileGetHandler = FileGetHandlerImpl(
                    logManager)
        }
        return fileGetHandler
    }

    companion object {

        @JvmStatic
        private var graph: ApplicationGraph? = null

        @JvmStatic
        fun getFileGetHandler(): FileGetHandler {
            if (graph == null) {
                graph = ApplicationGraph()
            }
            return graph!!.getFileGetHandlerInternal()
        }
    }

}