package com.mercandalli.android.sdk.files.api.online

import org.json.JSONArray

internal interface FileOnlineApi {

    fun get(): JSONArray?
}