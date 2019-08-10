package com.mercandalli.sdk.event.mercan

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class EventsBody(
    private val events: List<Event>
) {

    private val formatVersion = 0L

    fun getFormatVersion(): Long {
        return formatVersion
    }

    fun getEvents(): List<Event> {
        return ArrayList(events)
    }

    companion object {

        fun toSecuredJson(
            eventsBody: EventsBody,
            eventSecure: EventSecure
        ): JSONObject {
            val jsonObject = JSONObject()
            jsonObject.put("format_version", eventsBody.getFormatVersion())
            val eventsJsonArray = Event.toJson(eventsBody.getEvents())
            val contents = eventSecure.crypt(eventsJsonArray.toString())
            jsonObject.put("contents", contents)
            return jsonObject
        }

        fun fromSecuredJson(
            jsonObject: JSONObject,
            eventSecure: EventSecure
        ): EventsBody {
            val eventsJsonArrayString =
                when (val formatVersion = jsonObject.getLong("format_version")) {
                    0L -> {
                        jsonObject.getString("contents")
                    }
                    1L -> {
                        val contents = jsonObject.getString("contents")
                        eventSecure.decrypt(contents)
                    }
                    else -> throw JSONException("Not supported format. formatVersion: $formatVersion")
                }
            val eventsJsonArray = JSONArray(eventsJsonArrayString)
            val events = Event.fromJsonArray(eventsJsonArray)
            return EventsBody(
                events
            )
        }
    }
}
