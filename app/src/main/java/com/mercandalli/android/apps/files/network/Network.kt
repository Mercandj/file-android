package com.mercandalli.android.apps.files.network

interface Network {

    fun requestSync(
            url: String,
            headers: Map<String, String>
    ): String?
}