package com.mercandalli.sdk.event.mercan

import org.json.JSONException
import org.json.JSONObject

class EventReceiverImpl(
    private val eventSecure: EventSecure
) : EventReceiver {

    override fun receive(requestBody: String): List<Event> {
        val events = ArrayList<Event>()
        try {
            val bodyJsonObject = JSONObject(requestBody)
            val eventsBody = EventsBody.fromSecuredJson(bodyJsonObject, eventSecure)
            events.addAll(eventsBody.getEvents())
        } catch (e: JSONException) {
        }
        return events
    }
}
