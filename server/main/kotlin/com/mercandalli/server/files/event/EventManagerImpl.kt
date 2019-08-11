package com.mercandalli.server.files.event

import com.mercandalli.sdk.event.mercan.Event
import com.mercandalli.server.files.time.TimeManager

class EventManagerImpl(
    private val eventRepository: EventRepository,
    private val timeManager: TimeManager
) : EventManager {

    override fun handle(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        idAddress: String,
        events: List<Event>
    ): EventResponse {
        val serverTimeFileNameString = timeManager.getTimeFileNameString()
        val serverTimeMillis = timeManager.getTimeMillis()
        val eventsToSave = ArrayList<Event>()
        for (event in events) {
            val metadataString = HashMap(event.getMetadataString())
            metadataString["server_request_id_address"] = idAddress
            metadataString["server_request_server_time_readable"] = serverTimeFileNameString
            val metadataLong = HashMap(event.getMetadataLong())
            metadataLong["server_request_server_time_millis"] = serverTimeMillis
            val eventToSave = Event(
                event.getUuid(),
                event.getKey(),
                event.getValue(),
                event.getMetadataBoolean(),
                metadataLong,
                metadataString
            )
            eventsToSave.add(eventToSave)
        }
        return eventRepository.put(
            platform,
            applicationPackageName,
            applicationVersionName,
            events
        )
    }
}
