package com.mercandalli.sdk.event.mercan

import org.json.JSONArray
import org.json.JSONObject

class Event(
    private val uuid: String,
    private val key: String,
    private val value: String,
    private val metadataBoolean: Map<String, Boolean>,
    private val metadataLong: Map<String, Long>,
    private val metadataString: Map<String, String>
) {

    fun getUuid(): String {
        return uuid
    }

    companion object {

        fun toJson(event: Event): JSONObject {
            val jsonObject = JSONObject()
            jsonObject.put("uuid", event.uuid)
            jsonObject.put("key", event.key)
            jsonObject.put("value", event.value)

            val metadataBooleanJsonObject = JSONObject()
            for ((metadataKey, metadataValue) in event.metadataBoolean) {
                metadataBooleanJsonObject.put(metadataKey, metadataValue)
            }
            jsonObject.put("metadata_boolean", metadataBooleanJsonObject)

            val metadataLongJsonObject = JSONObject()
            for ((metadataKey, metadataValue) in event.metadataLong) {
                metadataLongJsonObject.put(metadataKey, metadataValue)
            }
            jsonObject.put("metadata_long", metadataLongJsonObject)

            val metadataStringJsonObject = JSONObject()
            for ((metadataKey, metadataValue) in event.metadataString) {
                metadataStringJsonObject.put(metadataKey, metadataValue)
            }
            jsonObject.put("metadata_string", metadataStringJsonObject)

            return jsonObject
        }

        fun toJson(events: List<Event>): JSONArray {
            val jsonArray = JSONArray()
            for (event in events) {
                jsonArray.put(toJson((event)))
            }
            return jsonArray
        }

        fun toJson(events: Map<String, Event>): JSONObject {
            val jsonObject = JSONObject()
            for ((uuid, event) in events) {
                jsonObject.put(uuid, toJson(event))
            }
            return jsonObject
        }

        fun fromJson(jsonObject: JSONObject): Event {
            val uuid = jsonObject.getString("uuid")
            val key = jsonObject.getString("key")
            val value = jsonObject.getString("value")

            val metadataBoolean = HashMap<String, Boolean>()
            val metadataBooleanJsonObject = jsonObject.getJSONObject("metadata_boolean")
            for (metadataKey in metadataBooleanJsonObject.keySet()) {
                val metadataValue = metadataBooleanJsonObject.getBoolean(key)
                metadataBoolean[metadataKey] = metadataValue
            }

            val metadataLong = HashMap<String, Long>()
            val metadataLongJsonObject = jsonObject.getJSONObject("metadata_long")
            for (metadataKey in metadataLongJsonObject.keySet()) {
                val metadataValue = metadataLongJsonObject.getLong(key)
                metadataLong[metadataKey] = metadataValue
            }

            val metadataString = HashMap<String, String>()
            val metadataStringJsonObject = jsonObject.getJSONObject("metadata_string")
            for (metadataKey in metadataStringJsonObject.keySet()) {
                val metadataValue = metadataStringJsonObject.getString(key)
                metadataString[metadataKey] = metadataValue
            }

            return Event(
                uuid,
                key,
                value,
                metadataBoolean,
                metadataLong,
                metadataString
            )
        }

        fun fromJsonArray(jsonArray: JSONArray): List<Event> {
            val events = ArrayList<Event>()
            for (i in 0 until jsonArray.length()) {
                val eventJsonObject = jsonArray.getJSONObject(i)
                val event = fromJson(eventJsonObject)
                events.add(event)
            }
            return events
        }
    }
}
