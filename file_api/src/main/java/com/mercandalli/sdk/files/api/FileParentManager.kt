package com.mercandalli.sdk.files.api

interface FileParentManager {

    fun getParentPath(
        path: String
    ): String?
}
