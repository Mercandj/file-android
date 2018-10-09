package com.mercandalli.server.files.response_json

import org.json.JSONObject

data class ResponseJsonObject(
        val content: JSONObject,
        val debugMessage: String
) {

    fun toJsonString() = toJson(this).toString()

    companion object {

        @JvmStatic
        fun toJson(responseJsonObject: ResponseJsonObject): JSONObject {
            val json = JSONObject()
            json.put("content", responseJsonObject.content)
            json.put("debug_message", responseJsonObject.debugMessage)
            return json
        }
    }
}