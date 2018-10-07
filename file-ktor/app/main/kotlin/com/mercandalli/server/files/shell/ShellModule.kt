package com.mercandalli.server.files.shell

import com.mercandalli.server.files.main.ApplicationGraph

class ShellModule {

    fun provideShellManager(): ShellManager {
        val logManager = ApplicationGraph.getLogManager()
        return ShellManagerImpl(logManager)
    }
}