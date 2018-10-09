package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFiles

internal interface FileOnlineApi {

    fun get(): ServerResponseFiles?
}