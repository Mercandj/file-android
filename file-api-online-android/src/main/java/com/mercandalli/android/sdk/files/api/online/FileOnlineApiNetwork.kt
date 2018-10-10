package com.mercandalli.android.sdk.files.api.online

import org.json.JSONObject

interface FileOnlineApiNetwork {

    fun getSync(
            url: String,
            headers: Map<String, String>
    ): String?

    fun postSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject
    ): String?
}