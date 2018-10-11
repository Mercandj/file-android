package com.mercandalli.server.files.time

class TimeModule {

    fun createTimeManager(): TimeManager {
        return TimeManagerImpl()
    }
}