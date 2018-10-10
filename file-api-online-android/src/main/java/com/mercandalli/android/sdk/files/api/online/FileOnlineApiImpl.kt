package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.online.FileOnlineLoginManager
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFiles
import org.json.JSONObject
import kotlin.collections.HashMap

internal class FileOnlineApiImpl(
        private val fileOnlineApiNetwork: FileOnlineApiNetwork,
        private val fileOnlineLoginManager: FileOnlineLoginManager
) : FileOnlineApi {

    override fun get(): ServerResponseFiles? {
        val headers = HashMap<String, String>()
        val token = fileOnlineLoginManager.createToken()
        headers["User-Agent"] = USER_AGENT
        headers["Content-Type"] = "application/json"
        headers["Authorization"] = "Basic $token"
        val body = fileOnlineApiNetwork.getSync(
                "$API_DOMAIN/file",
                headers
        ) ?: return null
        val jsonObject = JSONObject(body)
        return ServerResponseFiles.fromJson(jsonObject)
    }

    override fun post(file: File) {
        val headers = HashMap<String, String>()
        val token = fileOnlineLoginManager.createToken()
        headers["User-Agent"] = USER_AGENT
        headers["Content-Type"] = "application/json"
        headers["Authorization"] = "Basic $token"
        val fileJsonObject = File.toJson(file)
        fileOnlineApiNetwork.postSync(
                "$API_DOMAIN/file",
                headers,
                fileJsonObject
        )
    }

    companion object {

        private const val API_DOMAIN = "http://mercandalli.com/file-api"

        private const val USER_AGENT =
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36"
    }
}