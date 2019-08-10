package com.mercandalli.sdk.event.mercan

import java.util.UUID
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EventSenderImpl(
    private val url: String,
    private val eventNetwork: EventNetwork,
    private val eventSecure: EventSecure
) : EventSender {

    private val eventMetadataStringInterceptors = ArrayList<EventMetadataStringInterceptor>()
    private val eventMetadataBooleanInterceptors = ArrayList<EventMetadataBooleanInterceptor>()
    private val eventMetadataLongInterceptors = ArrayList<EventMetadataLongInterceptor>()

    override fun send(key: String, value: String) {
        val uuid = UUID.randomUUID().toString()
        val event = Event(
            uuid,
            key,
            value,
            createMetadataBoolean(),
            createMetadataLong(),
            createMetadataString()
        )
        send(event)
    }

    override fun addEventMetadataBooleanInterceptor(interceptor: EventMetadataBooleanInterceptor) {
        if (eventMetadataBooleanInterceptors.contains(interceptor)) {
            return
        }
        eventMetadataBooleanInterceptors.add(interceptor)
    }

    override fun addEventMetadataLongInterceptor(interceptor: EventMetadataLongInterceptor) {
        if (eventMetadataLongInterceptors.contains(interceptor)) {
            return
        }
        eventMetadataLongInterceptors.add(interceptor)
    }

    override fun addEventMetadataStringInterceptor(interceptor: EventMetadataStringInterceptor) {
        if (eventMetadataStringInterceptors.contains(interceptor)) {
            return
        }
        eventMetadataStringInterceptors.add(interceptor)
    }

    private fun send(event: Event) {
        val events = listOf(event)
        val bodyJsonObject = EventsBody.toSecuredJson(EventsBody(events), eventSecure)
        eventNetwork.postAsync(
            url,
            mapOf(),
            bodyJsonObject.toString()
        )
    }

    private fun createMetadataBoolean(): Map<String, Boolean> {
        val map = HashMap<String, Boolean>()
        for (interceptor in eventMetadataBooleanInterceptors) {
            map[interceptor.getKey()] = interceptor.getValue()
        }
        return map
    }

    private fun createMetadataLong(): Map<String, Long> {
        val map = HashMap<String, Long>()
        for (interceptor in eventMetadataLongInterceptors) {
            map[interceptor.getKey()] = interceptor.getValue()
        }
        return map
    }

    private fun createMetadataString(): Map<String, String> {
        val map = HashMap<String, String>()
        for (interceptor in eventMetadataStringInterceptors) {
            map[interceptor.getKey()] = interceptor.getValue()
        }
        return map
    }
}
