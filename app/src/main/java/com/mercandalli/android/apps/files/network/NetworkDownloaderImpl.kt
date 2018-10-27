package com.mercandalli.android.apps.files.network

import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.File

internal class NetworkDownloaderImpl(
        private val okHttpClientLazy: Lazy<OkHttpClient>
) : NetworkDownloader {

    override fun getDownloadSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject,
            javaFile: File,
            listener: Network.DownloadProgressListener
    ): String? {
        // TODO
        return null
    }
}