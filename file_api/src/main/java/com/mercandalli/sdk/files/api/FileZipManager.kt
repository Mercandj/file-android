package com.mercandalli.sdk.files.api

interface FileZipManager {

    fun isZip(
        path: String
    ): Boolean

    fun unzip(
        path: String,
        outputPath: String
    )

    fun registerFileZipListener(
        listener: FileZipListener
    )

    fun unregisterFileZipListener(
        listener: FileZipListener
    )

    interface FileZipListener {

        fun onUnzipEnded(
            path: String,
            outputPath: String
        )
    }
}
