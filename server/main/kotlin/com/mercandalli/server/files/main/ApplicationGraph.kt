package com.mercandalli.server.files.main

import com.mercandalli.sdk.files.api.online.FileOnlineAuthentication
import com.mercandalli.sdk.files.api.online.FileOnlineModule
import com.mercandalli.server.files.authorization.AuthorizationModule
import com.mercandalli.server.files.event.EventModule
import com.mercandalli.server.files.file_handler.FileHandlerModule
import com.mercandalli.server.files.file_repository.FileRepositoryModule
import com.mercandalli.server.files.log.LogModule
import com.mercandalli.server.files.main_argument.MainArgument
import com.mercandalli.server.files.server.ServerModule
import com.mercandalli.server.files.shell.ShellModule
import com.mercandalli.server.files.time.TimeModule
import com.mercandalli.sdk.feature_aes.AesModule

class ApplicationGraph(
    private val mainArgument: MainArgument,
    private val rootPath: String,
    private val pullSubRepositoryShellFile: java.io.File,
    private val fileOnlineAuthentications: List<FileOnlineAuthentication>
) {

    private val authorizationModule = AuthorizationModule()
    private val eventModule = EventModule()
    private val fileModule = FileHandlerModule()
    private val fileOnlineModule = FileOnlineModule()
    private val fileRepositoryModule = FileRepositoryModule(rootPath)
    private val logModule = LogModule()
    private val serverModule = ServerModule(rootPath)
    private val shellModule = ShellModule()
    private val timeModule = TimeModule()

    private val aesBase64ManagerInternal by lazy { AesModule().createAesBase64Manager() }
    private val authorizationManagerInternal by lazy { authorizationModule.createAuthorizationManager() }
    private val eventHandlerGetInternal by lazy { eventModule.createEventHandlerGet() }
    private val eventHandlerPostInternal by lazy { eventModule.createEventHandlerPost() }
    private val eventRepository by lazy { eventModule.createEventRepository() }
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

        fun getAesBase64Manager() = graph!!.aesBase64ManagerInternal
        fun getAuthorizationManager() = graph!!.authorizationManagerInternal
        fun getEventRepository() = graph!!.eventRepository
        fun getEventHandlerGet() = graph!!.eventHandlerGetInternal
        fun getEventHandlerPost() = graph!!.eventHandlerPostInternal
        fun getFileGetHandler() = graph!!.fileHandlerGetInternal
        fun getFilePostHandler() = graph!!.fileHandlerPostInternal
        fun getFileDeleteHandler() = graph!!.fileHandlerDeleteInternal
        fun getFileOnlineAuthentications() = graph!!.fileOnlineAuthentications
        fun getFileOnlineLoginManager() = graph!!.fileOnlineLoginManagerInternal
        fun getFileRepository() = graph!!.fileRepositoryInternal
        fun getLogManager() = graph!!.logManagerInternal
        fun getMainArgument() = graph!!.mainArgument
        fun getPullSubRepositoryShellFile() = graph!!.pullSubRepositoryShellFile
        fun getRootPath() = graph!!.rootPath
        fun getServerManager() = graph!!.serverManagerInternal
        fun getShellManager() = graph!!.shellManagerInternal
        fun getTimeManager() = graph!!.timeManagerInternal

        fun initialize(
            mainArgument: MainArgument,
            rootPath: String,
            pullSubRepositoryShellFile: java.io.File,
            fileOnlineAuthentications: List<FileOnlineAuthentication>
        ) {
            graph = ApplicationGraph(
                mainArgument,
                rootPath,
                pullSubRepositoryShellFile,
                fileOnlineAuthentications
            )
        }
    }
}
