package com.mercandalli.android.sdk.files.api.online

import org.json.JSONObject

internal interface FileOnlineApi {

    fun get(): JSONObject?
}