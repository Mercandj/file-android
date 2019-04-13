package com.mercandalli.sdk.files.api

interface FileShareManager {

    fun share(
        path: String
    )

    fun isShareSupported(
        path: String
    ): Boolean
}
