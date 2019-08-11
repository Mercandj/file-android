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
    private val eventContainer = EventContainer(
        fileName,
        eventRootFolder
    )

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
        return eventContainer.put(
            platform,
            applicationPackageName,
            applicationVersionName,
            events
        )
    }

    class EventContainer(
        private val fileName: String,
        private val folder: File,
        private val platformToEventPlatformContainer: HashMap<String, EventPlatformContainer> = HashMap()
    ) {

        fun put(
            platform: String,
            applicationPackageName: String,
            applicationVersionName: String,
            events: List<Event>
        ): EventResponse {
            val eventPlatformContainer = if (platformToEventPlatformContainer.containsKey(platform)) {
                platformToEventPlatformContainer.getValue(platform)
            } else {
                EventPlatformContainer(
                    fileName,
                    File(folder, platform),
                    platform
                )
            }
            val eventResponse = eventPlatformContainer.put(
                applicationPackageName,
                applicationVersionName,
                events
            )
            platformToEventPlatformContainer[platform] = eventPlatformContainer
            return eventResponse
        }
    }

    class EventPlatformContainer(
        private val fileName: String,
        private val folder: File,
        private val platform: String,
        private val applicationPackageNameToEventPlatformApplicationContainer: HashMap<String, EventPlatformApplicationContainer> = HashMap()
    ) {

        fun put(
            applicationPackageName: String,
            applicationVersionName: String,
            events: List<Event>
        ): EventResponse {
            val eventPlatformApplicationContainer = if (applicationPackageNameToEventPlatformApplicationContainer.containsKey(applicationPackageName)) {
                applicationPackageNameToEventPlatformApplicationContainer.getValue(applicationPackageName)
            } else {
                EventPlatformApplicationContainer(
                    fileName,
                    File(folder, applicationPackageName),
                    platform,
                    applicationPackageName
                )
            }
            val eventResponse = eventPlatformApplicationContainer.put(
                applicationVersionName,
                events
            )
            applicationPackageNameToEventPlatformApplicationContainer[applicationPackageName] = eventPlatformApplicationContainer
            return eventResponse
        }
    }

    class EventPlatformApplicationContainer(
        private val fileName: String,
        private val folder: File,
        private val platform: String,
        private val applicationPackageName: String,
        private val versionToEventPlatformApplicationVersionContainer: HashMap<String, EventPlatformApplicationVersionContainer> = HashMap()
    ) {

        fun put(
            applicationVersionName: String,
            events: List<Event>
        ): EventResponse {
            val eventPlatformApplicationVersionContainer = if (versionToEventPlatformApplicationVersionContainer.containsKey(applicationVersionName)) {
                versionToEventPlatformApplicationVersionContainer.getValue(applicationVersionName)
            } else {
                EventPlatformApplicationVersionContainer(
                    fileName,
                    File(folder, applicationVersionName),
                    platform,
                    applicationPackageName,
                    applicationVersionName
                )
            }
            val eventResponse = eventPlatformApplicationVersionContainer.put(
                events
            )
            versionToEventPlatformApplicationVersionContainer[applicationVersionName] = eventPlatformApplicationVersionContainer
            return eventResponse
        }
    }

    class EventPlatformApplicationVersionContainer(
        private val fileName: String,
        private val folder: File,
        private val platform: String,
        private val applicationPackageName: String,
        private val applicationVersionName: String,
        private val uuidToEvent: HashMap<String, Event> = HashMap()
    ) {

        fun put(
            events: List<Event>
        ): EventResponse {
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val file = File(folder, fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
            for (event in events) {
                uuidToEvent[event.getUuid()] = event
            }
            file.writeText(Event.toJson(uuidToEvent).toString())
            return EventResponse(
                true,
                events.size.toLong(),
                uuidToEvent.size.toLong()
            )
        }
    }
}
