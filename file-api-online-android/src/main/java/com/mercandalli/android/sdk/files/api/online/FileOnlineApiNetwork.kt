package com.mercandalli.android.sdk.files.api.online

import org.json.JSONObject

interface FileOnlineApiNetwork {

    fun getSync(
            url: String,
            headers: Map<String, String>
    ): String?

    fun getDownloadSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject,
            javaFile: java.io.File,
            listener: DownloadProgressListener
    ): String?

    fun postSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject
    ): String?

    fun postUploadSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject,
            javaFile: java.io.File,
            listener: UploadProgressListener
    ): String?

    fun deleteSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject
    ): String?

    interface DownloadProgressListener {

        fun onDownloadProgress(
                current: Long,
                size: Long
        )
    }

    interface UploadProgressListener {

        fun onUploadProgress(
                current: Long,
                size: Long
        )
    }
}