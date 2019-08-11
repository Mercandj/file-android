package com.mercandalli.server.files.event

import com.mercandalli.sdk.event.mercan.EventReceiver
import com.mercandalli.server.files.log.LogManager

class EventHandlerPostImpl(
    private val eventManager: EventManager,
    private val eventReceiver: EventReceiver,
    private val logManager: LogManager
) : EventHandlerPost {

    override fun handleEvent(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        idAddress: String,
        body: String
    ): EventResponse {
        logManager.d("EventHandlerPost", "$platform $applicationPackageName $applicationVersionName $body")
        val events = eventReceiver.receive(body)
        return eventManager.handle(
            platform,
            applicationPackageName,
            applicationVersionName,
            idAddress,
            events
        )
    }
}
