package com.mercandalli.sdk.files.api

interface FileOpenManager {

    fun open(
        path: String,
        mime: String? = null
    )
}
