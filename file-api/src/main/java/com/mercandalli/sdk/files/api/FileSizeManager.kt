package com.mercandalli.sdk.files.api

interface FileSizeManager {

    fun loadSize(path: String, forceRefresh: Boolean = false): FileSizeResult

    fun getSize(path: String): FileSizeResult

    fun registerFileSizeResultListener(listener: FileSizeResultListener)

    fun unregisterFileSizeResultListener(listener: FileSizeResultListener)

    interface FileSizeResultListener {

        fun onFileSizeResultChanged(path: String)
    }
}