package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.File

interface FileOnlineUploadManager {

    fun upload(
        inputJavaFile: java.io.File,
        outputFile: File
    )

    fun registerListener(listener: UploadListener)

    fun unregisterListener(listener: UploadListener)

    interface UploadListener {

        fun onUploadStarted(
            file: File
        )

        fun onUploadProgress(
            file: File,
            current: Long,
            size: Long
        )

        fun onUploadEnded(
            file: File
        )
    }
}
