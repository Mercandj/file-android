package com.mercandalli.sdk.files.api

interface FileManager {

    fun loadFile(
        path: String,
        forceRefresh: Boolean = false
    ): FileResult

    fun getFile(
        path: String
    ): FileResult

    fun registerFileResultListener(
        listener: FileResultListener
    )

    fun unregisterFileResultListener(
        listener: FileResultListener
    )

    interface FileResultListener {

        fun onFileResultChanged(
            path: String
        )
    }
}
