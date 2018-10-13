package com.mercandalli.sdk.files.api

interface FileSizeManager {

    fun computeSize(path: String)

    fun getSize(path: String): FileSizeResult

    fun registerFileSizeResultListener(listener: FileSizeResultListener)

    fun unregisterFileSizeResultListener(listener: FileSizeResultListener)

    interface FileSizeResultListener {

        fun onFileSizeResultChanged(path: String)
    }
}