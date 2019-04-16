package com.mercandalli.sdk.files.api

interface FilePathManager {

    fun getParentPath(
        path: String
    ): String?

    fun createPath(
        parentPath: String,
        fileName: String
    ): String?
}
