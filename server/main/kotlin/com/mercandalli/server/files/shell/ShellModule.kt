package com.mercandalli.server.files.shell

import com.mercandalli.server.files.main.ApplicationGraph

class ShellModule {

    private val logManager by lazy { ApplicationGraph.getLogManager() }

    fun createShellManager(): ShellManager {
        return ShellManagerImpl(
                logManager
        )
    }
}