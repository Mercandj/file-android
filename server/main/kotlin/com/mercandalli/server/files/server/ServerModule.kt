package com.mercandalli.server.files.server

import com.mercandalli.server.files.main.ApplicationGraph

class ServerModule(
        private val rootPath: String
) {

    private val fileHandlerGet by lazy { ApplicationGraph.getFileGetHandler() }
    private val fileHandlerPost by lazy { ApplicationGraph.getFilePostHandler() }
    private val fileOnlineLoginManager by lazy { ApplicationGraph.getFileOnlineLoginManager() }
    private val shellManager by lazy { ApplicationGraph.getShellManager() }
    private val logManager by lazy { ApplicationGraph.getLogManager() }
    private val pullSubRepositoryShellFile by lazy { ApplicationGraph.getPullSubRepositoryShellFile() }

    fun createServerManager(): ServerManager {
        return ServerManagerImpl(
                rootPath,
                fileHandlerGet,
                fileHandlerPost,
                fileOnlineLoginManager,
                shellManager,
                logManager,
                pullSubRepositoryShellFile
        )
    }
}