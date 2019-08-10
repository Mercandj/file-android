package com.mercandalli.server.files.event

import com.mercandalli.sdk.event.mercan.Event
import com.mercandalli.server.files.time.TimeManager
import java.io.File

class EventRepositoryImpl(
    timeManager: TimeManager,
    rootFolder: File
) : EventRepository {

    private val fileName by lazy { "events_${timeManager.getTimeFileNameString()}.json" }
    private val eventRootFolder by lazy { File(rootFolder, "event-repository") }
    private val uuidToEvent = HashMap<String, Event>()

    override fun put(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        events: List<Event>
    ): EventResponse {
        if (events.isEmpty()) {
            return EventResponse(
                false,
                0,
                0
            )
        }
        for (event in events) {
            uuidToEvent[event.getUuid()] = event
        }
        val platformFolder = File(eventRootFolder, platform)
        val applicationPackageNameFolder = File(platformFolder, applicationPackageName)
        val applicationVersionNameFolder = File(applicationPackageNameFolder, applicationVersionName)
        if (!applicationVersionNameFolder.exists()) {
            applicationVersionNameFolder.mkdirs()
        }
        val eventsFile = File(applicationVersionNameFolder, fileName)
        if (!eventsFile.exists()) {
            eventsFile.createNewFile()
        }
        eventsFile.writeText(Event.toJson(uuidToEvent).toString())
        return EventResponse(
            true,
            events.size.toLong(),
            uuidToEvent.size.toLong()
        )
    }
}
