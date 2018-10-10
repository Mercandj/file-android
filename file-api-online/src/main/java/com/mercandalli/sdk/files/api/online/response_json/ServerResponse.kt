package com.mercandalli.sdk.files.api.online.response_json

import org.json.JSONObject

data class ServerResponse private constructor(
        val content: JSONObject,
        val debugMessage: String,
        val succeeded: Boolean
) {

    fun toJsonString() = toJson(this).toString()

    companion object {

        @JvmStatic
        fun toJson(serverResponse: ServerResponse): JSONObject {
            val json = JSONObject()
            json.put("content", serverResponse.content)
            json.put("debug_message", serverResponse.debugMessage)
            json.put("succeeded", serverResponse.succeeded)
            return json
        }

        @JvmStatic
        fun fromJson(jsonObject: JSONObject): ServerResponse {
            val content = jsonObject.getJSONObject("content")
            val debugMessage = jsonObject.getString("debug_message")
            val succeeded = jsonObject.getBoolean("succeeded")
            return ServerResponse(
                    content,
                    debugMessage,
                    succeeded
            )
        }

        @JvmStatic
        fun create(
                content: JSONObject,
                debugMessage: String,
                succeeded: Boolean
        ): ServerResponse {
            return ServerResponse(
                    content,
                    debugMessage,
                    succeeded
            )
        }
    }
}