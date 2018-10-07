package com.mercandalli.server.files.main

import com.mercandalli.sdk.files.api.online.FileOnlineModule
import com.mercandalli.server.files.file.FileModule
import com.mercandalli.server.files.log.LogModule
import com.mercandalli.server.files.server.ServerModule
import com.mercandalli.server.files.shell.ShellModule

class ApplicationGraph(
        rootPath: String
) {

    private val fileModule = FileModule()
    private val fileOnlineModule = FileOnlineModule()
    private val logModule = LogModule()
    private val serverModule = ServerModule(rootPath)
    private val shellModule = ShellModule()

    private val fileGetHandlerInternal by lazy { fileModule.createFileGetHandler() }
    private val fileOnlineLoginManagerInternal by lazy { fileOnlineModule.createFileOnlineLoginManager() }
    private val logManagerInternal by lazy { logModule.createLogManager() }
    private val serverManagerInternal by lazy { serverModule.createServerManager() }
    private val shellManagerInternal by lazy { shellModule.provideShellManager() }

    companion object {

        private var graph: ApplicationGraph? = null

        fun getFileGetHandler() = graph!!.fileGetHandlerInternal
        fun getFileOnlineLoginManager() = graph!!.fileOnlineLoginManagerInternal
        fun getLogManager() = graph!!.logManagerInternal
        fun getServerManager() = graph!!.serverManagerInternal
        fun getShellManager() = graph!!.shellManagerInternal

        fun initialize(rootPath: String) {
            graph = ApplicationGraph(rootPath)
        }
    }
}