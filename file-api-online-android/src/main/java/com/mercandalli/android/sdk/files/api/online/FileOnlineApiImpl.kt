package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.online.FileOnlineLoginManager
import org.json.JSONArray
import kotlin.collections.HashMap

internal class FileOnlineApiImpl(
        private val fileOnlineApiNetwork: FileOnlineApiNetwork,
        private val fileOnlineLoginManager: FileOnlineLoginManager
) : FileOnlineApi {

    override fun get(): JSONArray? {
        val headers = HashMap<String, String>()
        val token = fileOnlineLoginManager.createToken()
        headers["User-Agent"] = USER_AGENT
        headers["Content-Type"] = "application/json"
        headers["Authorization"] = "Basic $token"
        val body = fileOnlineApiNetwork.requestSync(
                "$API_DOMAIN/file",
                headers
        )
        return if (body == null) {
            null
        } else {
            JSONArray(body)
        }
    }

    companion object {

        private const val API_DOMAIN = "http://mercandalli.com/file-api"

        private const val USER_AGENT =
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36"
    }
}