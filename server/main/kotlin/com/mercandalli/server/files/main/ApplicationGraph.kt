package com.mercandalli.server.files.main

import com.mercandalli.sdk.files.api.online.FileOnlineModule
import com.mercandalli.server.files.file_handler.FileHandlerModule
import com.mercandalli.server.files.file_repository.FileRepositoryModule
import com.mercandalli.server.files.log.LogModule
import com.mercandalli.server.files.server.ServerModule
import com.mercandalli.server.files.shell.ShellModule
import com.mercandalli.server.files.time.TimeModule

class ApplicationGraph(
        val rootPath: String,
        val pullSubRepositoryShellFile: java.io.File
) {

    private val fileModule = FileHandlerModule()
    private val fileOnlineModule = FileOnlineModule()
    private val fileRepositoryModule = FileRepositoryModule(rootPath)
    private val logModule = LogModule()
    private val serverModule = ServerModule(rootPath)
    private val shellModule = ShellModule()
    private val timeModule = TimeModule()

    private val fileHandlerGetInternal by lazy { fileModule.createFileHandlerGet() }
    private val fileHandlerPostInternal by lazy { fileModule.createFileHandlerPost() }
    private val fileHandlerDeleteInternal by lazy { fileModule.createFileHandlerDelete() }
    private val fileOnlineLoginManagerInternal by lazy { fileOnlineModule.createFileOnlineLoginManager() }
    private val fileRepositoryInternal by lazy { fileRepositoryModule.createFileRepository() }
    private val logManagerInternal by lazy { logModule.createLogManager() }
    private val serverManagerInternal by lazy { serverModule.createServerManager() }
    private val shellManagerInternal by lazy { shellModule.createShellManager() }
    private val timeManagerInternal by lazy { timeModule.createTimeManager() }

    companion object {

        private var graph: ApplicationGraph? = null

        fun getFileGetHandler() = graph!!.fileHandlerGetInternal
        fun getFilePostHandler() = graph!!.fileHandlerPostInternal
        fun getFileDeleteHandler() = graph!!.fileHandlerDeleteInternal
        fun getFileOnlineLoginManager() = graph!!.fileOnlineLoginManagerInternal
        fun getFileRepository() = graph!!.fileRepositoryInternal
        fun getLogManager() = graph!!.logManagerInternal
        fun getPullSubRepositoryShellFile() = graph!!.pullSubRepositoryShellFile
        fun getRootPath() = graph!!.rootPath
        fun getServerManager() = graph!!.serverManagerInternal
        fun getShellManager() = graph!!.shellManagerInternal
        fun getTimeManager() = graph!!.timeManagerInternal

        fun initialize(
                rootPath: String,
                pullSubRepositoryShellFile: java.io.File
        ) {
            graph = ApplicationGraph(
                    rootPath,
                    pullSubRepositoryShellFile
            )
        }
    }
}