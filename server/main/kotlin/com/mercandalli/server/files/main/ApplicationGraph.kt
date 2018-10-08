package com.mercandalli.server.files.main

import com.mercandalli.sdk.files.api.online.FileOnlineModule
import com.mercandalli.server.files.file_handler.FileHandlerModule
import com.mercandalli.server.files.file_repository.FileRepositoryModule
import com.mercandalli.server.files.log.LogModule
import com.mercandalli.server.files.server.ServerModule
import com.mercandalli.server.files.shell.ShellModule

class ApplicationGraph(
        val rootPath: String
) {

    private val fileModule = FileHandlerModule()
    private val fileOnlineModule = FileOnlineModule()
    private val fileRepositoryModule = FileRepositoryModule(rootPath)
    private val logModule = LogModule()
    private val serverModule = ServerModule(rootPath)
    private val shellModule = ShellModule()

    private val fileHandlerGetInternal by lazy { fileModule.createFileHandlerGet() }
    private val fileHandlerPostInternal by lazy { fileModule.createFileHandlerPost() }
    private val fileOnlineLoginManagerInternal by lazy { fileOnlineModule.createFileOnlineLoginManager() }
    private val fileRepositoryInternal by lazy { fileRepositoryModule.createFileRepository() }
    private val logManagerInternal by lazy { logModule.createLogManager() }
    private val serverManagerInternal by lazy { serverModule.createServerManager() }
    private val shellManagerInternal by lazy { shellModule.provideShellManager() }

    companion object {

        private var graph: ApplicationGraph? = null

        fun getFileGetHandler() = graph!!.fileHandlerGetInternal
        fun getFilePostHandler() = graph!!.fileHandlerPostInternal
        fun getFileOnlineLoginManager() = graph!!.fileOnlineLoginManagerInternal
        fun getFileRepository() = graph!!.fileRepositoryInternal
        fun getLogManager() = graph!!.logManagerInternal
        fun getRootPath() = graph!!.rootPath
        fun getServerManager() = graph!!.serverManagerInternal
        fun getShellManager() = graph!!.shellManagerInternal

        fun initialize(rootPath: String) {
            graph = ApplicationGraph(rootPath)
        }
    }
}