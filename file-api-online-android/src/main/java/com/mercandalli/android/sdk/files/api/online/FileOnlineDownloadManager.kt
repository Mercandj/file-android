package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.File

interface FileOnlineDownloadManager {

    fun download(
            inputFile: File,
            outputJavaFile: java.io.File
    )

    fun registerListener(listener: DownloadListener)

    fun unregisterListener(listener: DownloadListener)

    interface DownloadListener {

        fun onDownloadStarted(
                file: File
        )

        fun onDownloadProgress(
                file: File,
                current: Long,
                size: Long
        )

        fun onDownloadEnded(
                file: File
        )
    }
}