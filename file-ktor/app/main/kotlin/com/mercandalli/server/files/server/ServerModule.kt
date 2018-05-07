package com.mercandalli.server.files.server

import com.mercandalli.server.files.file.FileGetHandler

class ServerModule(
        private val rootPath: String,
        private val fileGetHandler: FileGetHandler
) {

    fun provideServerManager(): ServerManager {
        return ServerManagerImpl(
                rootPath,
                fileGetHandler
        )
    }
}