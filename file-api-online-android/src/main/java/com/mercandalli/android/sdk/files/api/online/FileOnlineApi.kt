package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.online.response_json.ServerResponse
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFiles

internal interface FileOnlineApi {

    fun get(): ServerResponseFiles?

    fun getFromParent(
            parentPath: String
    ): ServerResponseFiles?

    fun getSize(
            path: String
    ): ServerResponse?

    fun getDownload(
            inputFilePath: String,
            outputJavaFile: java.io.File,
            listener:DownloadProgressListener
    )

    fun post(
            file: File
    )

    fun postUpload(
            inputJavaFile: java.io.File,
            outputFile: File,
            listener: UploadProgressListener
    )

    fun delete(
            path: String
    ): Boolean

    fun rename(
            path: String,
            name: String
    ): Boolean

    fun copy(
            pathInput: String,
            pathDirectoryOutput: String
    ): Boolean

    fun cut(
            pathInput: String,
            pathDirectoryOutput: String
    ): Boolean

    interface DownloadProgressListener {

        fun onDownloadProgress(
                inputFilePath: String,
                current: Long,
                size: Long
        )
    }

    interface UploadProgressListener {

        fun onUploadProgress(
                file: File,
                current: Long,
                size: Long
        )
    }
}