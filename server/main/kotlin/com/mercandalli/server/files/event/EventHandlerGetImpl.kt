package com.mercandalli.server.files.event

import com.mercandalli.server.files.log.LogManager

class EventHandlerGetImpl(
    private val eventManager: EventManager,
    private val logManager: LogManager
) : EventHandlerGet {

    override fun get(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String
    ): List<String> {
        logManager.d("EventHandlerGet", "$platform $applicationPackageName $applicationVersionName")
        return eventManager.get(
            platform,
            applicationPackageName,
            applicationVersionName
        )
    }
}
