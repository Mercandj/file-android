package com.mercandalli.server.files.server

import com.mercandalli.server.files.main.ApplicationGraph

class ServerModule(
        private val rootPath: String
) {

    fun createServerManager(): ServerManager {
        val fileGetHandler = ApplicationGraph.getFileGetHandler()
        val fileOnlineLoginManager=ApplicationGraph.getFileOnlineLoginManager()
        return ServerManagerImpl(
                rootPath,
                fileGetHandler,
                fileOnlineLoginManager
        )
    }
}