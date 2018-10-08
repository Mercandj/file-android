package com.mercandalli.server.files.server

import com.mercandalli.server.files.main.ApplicationGraph

class ServerModule(
        private val rootPath: String
) {

    private val fileHandlerGet by lazy { ApplicationGraph.getFileGetHandler() }
    private val fileHandlerPost by lazy { ApplicationGraph.getFilePostHandler() }
    private val fileOnlineLoginManager by lazy { ApplicationGraph.getFileOnlineLoginManager() }

    fun createServerManager(): ServerManager {
        return ServerManagerImpl(
                rootPath,
                fileHandlerGet,
                fileHandlerPost,
                fileOnlineLoginManager
        )
    }
}