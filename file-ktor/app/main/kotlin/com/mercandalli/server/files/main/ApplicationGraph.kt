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

    private val logManagerInternal: LogManager by lazy {
        LogManagerImpl()
    }

    private val shellManagerInternal: ShellManager by lazy {
        ShellModule(
                logManagerInternal
        ).provideShellManager()
    }

    private val serverManagerInternal: ServerManager by lazy {
        ServerModule(
                rootPath,
                fileGetHandlerInternal
        ).provideServerManager()
    }

    private val fileGetHandlerInternal: FileGetHandler by lazy {
        FileGetHandlerImpl(
                logManagerInternal
        )
    }

    companion object {

        @JvmStatic
        private var graph: ApplicationGraph? = null

        @JvmStatic
        fun initialize(rootPath: String) {
            graph = ApplicationGraph(rootPath)
        }

        @JvmStatic
        fun getLogManager() = graph!!.logManagerInternal

        @JvmStatic
        fun getShellManager() = graph!!.shellManagerInternal

        @JvmStatic
        fun getServerManager() = graph!!.serverManagerInternal
    }
}