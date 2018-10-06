package com.mercandalli.android.sdk.files.api.online

interface FileOnlineApiNetwork {

    fun requestSync(
            url: String,
            headers: Map<String, String>
    ): String?
}