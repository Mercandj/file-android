package com.mercandalli.server.files.event

import com.mercandalli.sdk.event.mercan.Event

class EventManagerImpl(
    private val eventRepository: EventRepository
) : EventManager {

    override fun handle(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        events: List<Event>
    ): EventResponse {
        return eventRepository.put(
            platform,
            applicationPackageName,
            applicationVersionName,
            events
        )
    }
}
