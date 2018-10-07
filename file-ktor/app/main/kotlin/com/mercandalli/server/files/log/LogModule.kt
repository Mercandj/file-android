package com.mercandalli.server.files.log

class LogModule {

    fun createLogManager(): LogManager {
        return LogManagerImpl()
    }
}