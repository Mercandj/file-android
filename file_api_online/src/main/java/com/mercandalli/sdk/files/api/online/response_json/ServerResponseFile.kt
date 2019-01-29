@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.sdk.files.api.online.response_json

import com.mercandalli.sdk.files.api.File
import org.json.JSONObject

data class ServerResponseFile private constructor(
    val file: File,
    private val serverResponse: ServerResponse
) {

    fun toJsonString() = toJson(this).toString()

    companion object {

        @JvmStatic
        fun create(
            file: File,
            debugMessage: String,
            succeeded: Boolean
        ): ServerResponseFile {
            val content = JSONObject()
            content.put("file", File.toJson(file))
            val serverResponse = ServerResponse.create(
                content,
                debugMessage,
                succeeded
            )
            return ServerResponseFile(
                file,
                serverResponse
            )
        }

        @JvmStatic
        fun toJson(serverResponseFiles: ServerResponseFile): JSONObject {
            return ServerResponse.toJson(serverResponseFiles.serverResponse)
        }

        @JvmStatic
        fun fromJson(jsonObject: JSONObject): ServerResponseFile {
            val serverResponse = ServerResponse.fromJson(jsonObject)
            val content = serverResponse.content
            val file = File.fromJson(content.getJSONObject("file"))
            return ServerResponseFile(
                file,
                serverResponse
            )
        }
    }
}
