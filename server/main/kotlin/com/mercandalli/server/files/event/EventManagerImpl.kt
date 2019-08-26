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

    override fun get(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String
    ): List<String> {
        val lines = ArrayList<String>()
        val events = eventRepository.get(
            platform, applicationPackageName, applicationVersionName
        )
        val eventCount = events.size.toLong()
        val distinctInstallationIdCount = extractDistinctInstallationIdCount(events)
        val keyToCount = extractKeyToCount(events)
        val keyToDistinctUserCount = extractKeyToDistinctUserCount(events)

        lines.add("----------------------------------------------------------------------------------------------------")
        lines.add("eventCount -> distinctInstallationIdCount      $eventCount\t$distinctInstallationIdCount")
        lines.add("----------------------------------------------------------------------------------------------------")
        for (key in keyToCount.keys.sorted()) {
            val count = keyToCount.getValue(key)
            val distinctUserCount = keyToDistinctUserCount.getValue(key)
            lines.add("count -> distinctUserCount -> key              $count\t$distinctUserCount\t$key")
        }
        lines.add("----------------------------------------------------------------------------------------------------")
        val sessionJvmIndexToDistinctInstallationIdCount =
            extractSessionJvmIndexToDistinctInstallationIdCount(events)
        val sessionJvmIndexes = ArrayList(sessionJvmIndexToDistinctInstallationIdCount.keys).sorted()
        for (sessionJvmIndex in sessionJvmIndexes) {
            val distinctUserCount = sessionJvmIndexToDistinctInstallationIdCount[sessionJvmIndex]
            lines.add("sessionJvmIndex -> distinctUserCount           $sessionJvmIndex\t$distinctUserCount")
        }
        return lines
    }

    private fun extractDistinctInstallationIds(events: List<Event>): Set<String> {
        val installationIds = HashSet<String>()
        for (event in events) {
            installationIds.add(event.getInstallationId())
        }
        return installationIds
    }

    private fun extractDistinctInstallationIdCount(events: List<Event>): Long {
        return extractDistinctInstallationIds(events).size.toLong()
    }

    private fun extractSessionJvmIndexToDistinctInstallationIdCount(events: List<Event>): Map<Long, Long> {
        val installationIdToSessionJvmIndex = HashMap<String, Long>()
        for (event in events) {
            val installationId = event.getInstallationId()
            val sessionJvmIndex = event.getSessionJvmIndex()
            if (installationIdToSessionJvmIndex.containsKey(installationId)) {
                val currentSessionJvmIndex =
                    installationIdToSessionJvmIndex.getValue(installationId)
                if (sessionJvmIndex > currentSessionJvmIndex) {
                    installationIdToSessionJvmIndex[installationId] = sessionJvmIndex
                }
            } else {
                installationIdToSessionJvmIndex[installationId] = sessionJvmIndex
            }
        }
        val sessionJvmIndexToDistinctInstallationIds = HashMap<Long, HashSet<String>>()
        for ((installationId, sessionJvmIndex) in installationIdToSessionJvmIndex) {
            val installationIds =
                if (sessionJvmIndexToDistinctInstallationIds.containsKey(sessionJvmIndex)) {
                    sessionJvmIndexToDistinctInstallationIds.getValue(sessionJvmIndex)
                } else {
                    HashSet()
                }
            installationIds.add(installationId)
            sessionJvmIndexToDistinctInstallationIds[sessionJvmIndex] = installationIds
        }
        val sessionJvmIndexToDistinctInstallationIdCount = HashMap<Long, Long>()
        for ((sessionJvmIndex, distinctInstallationIds) in sessionJvmIndexToDistinctInstallationIds) {
            sessionJvmIndexToDistinctInstallationIdCount[sessionJvmIndex] =
                distinctInstallationIds.size.toLong()
        }
        return sessionJvmIndexToDistinctInstallationIdCount
    }

    private fun extractKeyToCount(events: List<Event>): Map<String, Long> {
        val keyToCount = HashMap<String, Long>()
        for (event in events) {
            val key = event.getKey()
            val count = if (keyToCount.containsKey(key)) {
                keyToCount.getValue(key)
            } else {
                0
            }
            keyToCount[key] = count + 1L
        }
        return keyToCount
    }

    private fun extractKeyToDistinctUserCount(events: List<Event>): Map<String, Long> {
        val keyToDistinctUserCount = HashMap<String, Long>()
        val keyToInstallationIds = HashMap<String, HashSet<String>>()
        for (event in events) {
            val key = event.getKey()
            val installationId = event.getInstallationId()
            val installationIds = if (keyToInstallationIds.containsKey(key)) {
                keyToInstallationIds.getValue(key)
            } else {
                HashSet()
            }
            installationIds.add(installationId)
            keyToInstallationIds[key] = installationIds
        }
        for ((key, installationIds) in keyToInstallationIds) {
            keyToDistinctUserCount[key] = installationIds.size.toLong()
        }
        return keyToDistinctUserCount
    }

    private fun Event.getInstallationId(): String {
        return getMetadataString().getValue("installation_id")
    }

    private fun Event.getSessionJvmIndex(): Long {
        return getMetadataLong().getValue("session_jvm_index")
    }
}
