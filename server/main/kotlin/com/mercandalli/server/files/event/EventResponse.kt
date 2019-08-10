package com.mercandalli.server.files.event

class EventResponse(
    private val succeeded: Boolean,
    private val eventsAddedCount: Long,
    private val eventsWrittenCount: Long
) {

    fun isSucceeded() = succeeded

    fun getEventsAddedCount() = eventsAddedCount

    fun getEventsWrittenCount() = eventsWrittenCount
}
