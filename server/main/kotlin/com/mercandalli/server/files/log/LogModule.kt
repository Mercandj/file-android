package com.mercandalli.server.files.log

import com.mercandalli.server.files.main.ApplicationGraph

class LogModule {

    private val timeManager by lazy { ApplicationGraph.getTimeManager() }

    fun createLogManager(): LogManager {
        val rootPath = ApplicationGraph.getRootPath()
        return LogManagerImpl(
            rootPath,
            timeManager
        )
    }
}
