package com.mercandalli.sdk.files.api.online.response_json

import org.json.JSONObject

data class ServerResponse private constructor(
        val content: JSONObject,
        val debugMessage: String
) {

    fun toJsonString() = toJson(this).toString()

    companion object {

        @JvmStatic
        fun toJson(serverResponse: ServerResponse): JSONObject {
            val json = JSONObject()
            json.put("content", serverResponse.content)
            json.put("debug_message", serverResponse.debugMessage)
            return json
        }

        @JvmStatic
        fun fromJson(jsonObject: JSONObject): ServerResponse {
            val content = jsonObject.getJSONObject("content")
            val debugMessage = jsonObject.getString("debug_message")
            return ServerResponse(
                    content,
                    debugMessage
            )
        }

        @JvmStatic
        fun create(
                content: JSONObject,
                debugMessage: String
        ): ServerResponse {
            return ServerResponse(
                    content,
                    debugMessage
            )
        }
    }
}