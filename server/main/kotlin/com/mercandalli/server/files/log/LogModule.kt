package com.mercandalli.server.files.log

import com.mercandalli.server.files.main.ApplicationGraph

class LogModule {

    fun createLogManager(): LogManager {
        val rootPath = ApplicationGraph.getRootPath()
        return LogManagerImpl(
                rootPath
        )
    }
}