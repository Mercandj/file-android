package com.mercandalli.android.apps.files.network

import org.json.JSONObject

interface Network {

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