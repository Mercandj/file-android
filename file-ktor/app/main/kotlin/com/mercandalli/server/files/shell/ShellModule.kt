package com.mercandalli.server.files.shell

import com.mercandalli.server.files.log.LogManager

class ShellModule(
        private val logManager: LogManager
) {

    fun provideShellManager(): ShellManager {
        return ShellManagerImpl(logManager)
    }
}