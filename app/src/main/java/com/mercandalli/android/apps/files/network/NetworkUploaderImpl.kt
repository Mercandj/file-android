package com.mercandalli.android.apps.files.network

import android.util.Log
import android.webkit.MimeTypeMap
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.Headers
import okhttp3.ResponseBody
import okhttp3.RequestBody
import okhttp3.Request
import org.json.JSONObject
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException

internal class NetworkUploaderImpl(
        private val okHttpClientLazy: Lazy<OkHttpClient>
) : NetworkUploader {

    override fun postUploadSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject,
            javaFile: File,
            listener: Network.UploadProgressListener
    ): String? {
        val mimeTypeString = getMimeType(javaFile.absolutePath)
        val mimeType = if (mimeTypeString == null) {
            MediaType.parse("*/*")
        } else {
            MediaType.parse(mimeTypeString)
        }
        val req = MultipartBody
                .Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "json",
                        jsonObject.toString()
                )
                .addFormDataPart(
                        "file",
                        javaFile.name,
                        RequestBody.create(mimeType, javaFile)
                )
                .build()
        val request = Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(req)
                .build()
        return call(request)
    }

    private fun call(request: Request): String? {
        var response: Response? = null
        var body: ResponseBody? = null
        try {
            response = okHttpClientLazy.value.newCall(request).execute()
            body = response!!.body()
            return body!!.string()
        } catch (e: IOException) {
            Log.e("jm/debug", "IOException", e)
        } catch (e: SocketTimeoutException) {
            Log.e("jm/debug", "SocketTimeoutException", e)
        } finally {
            closeSilently(body, response)
        }
        return null
    }

    companion object {

        private fun getMimeType(url: String): String? {
            var type: String? = null
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
            return type
        }

        private fun closeSilently(vararg xs: Closeable?) {
            for (x in xs) {
                try {
                    x?.close()
                } catch (ignored: Throwable) {
                }
            }
        }
    }
}