package com.mercandalli.server.files.main

import com.mercandalli.server.files.file.FileGetHandler
import com.mercandalli.server.files.file.FileGetHandlerImpl
import com.mercandalli.server.files.log.LogManager
import com.mercandalli.server.files.log.LogManagerImpl
import com.mercandalli.server.files.server.ServerManager
import com.mercandalli.server.files.server.ServerModule
import com.mercandalli.server.files.shell.ShellManager
import com.mercandalli.server.files.shell.ShellModule

class ApplicationGraph(
        private val rootPath: String
) {

    private lateinit var logManager: LogManager
    private lateinit var shellManager: ShellManager
    private lateinit var serverManager: ServerManager
    private lateinit var fileGetHandler: FileGetHandler

    private fun getLogManagerInternal(): LogManager {
        if (!::logManager.isInitialized) {
            logManager = LogManagerImpl()
        }
        return logManager
    }

    private fun getShellManagerInternal(): ShellManager {
        if (!::shellManager.isInitialized) {
            val logManager = getLogManagerInternal()
            shellManager = ShellModule(
                    logManager
            ).provideShellManager()
        }
        return shellManager
    }

    private fun getServerManagerInternal(): ServerManager {
        if (!::serverManager.isInitialized) {
            val fileGetHandler = getFileGetHandlerInternal()
            serverManager = ServerModule(
                    rootPath,
                    fileGetHandler
            ).provideServerManager()
        }
        return serverManager
    }

    private fun getFileGetHandlerInternal(): FileGetHandler {
        if (!::fileGetHandler.isInitialized) {
            val logManager = getLogManagerInternal()
            fileGetHandler = FileGetHandlerImpl(
                    logManager
            )
        }
        return fileGetHandler
    }

    companion object {

        @JvmStatic
        private var graph: ApplicationGraph? = null

        @JvmStatic
        fun initialize(rootPath: String) {
            graph = ApplicationGraph(rootPath)
        }

        @JvmStatic
        fun getLogManager(): LogManager {
            return graph!!.getLogManagerInternal()
        }

        @JvmStatic
        fun getShellManager(): ShellManager {
            return graph!!.getShellManagerInternal()
        }

        @JvmStatic
        fun getServerManager(): ServerManager {
            return graph!!.getServerManagerInternal()
        }
    }

}