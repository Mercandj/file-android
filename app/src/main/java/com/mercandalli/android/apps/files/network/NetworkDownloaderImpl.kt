package com.mercandalli.android.apps.files.network

import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

internal class NetworkDownloaderImpl(
        private val okHttpClientLazy: Lazy<OkHttpClient>
) : NetworkDownloader {

    override fun postDownloadSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject,
            outputJavaFile: File,
            listener: Network.DownloadProgressListener
    ): String? {
        val body = RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString())
        val request = Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(body)
                .build()
        val call = okHttpClientLazy.value.newCall(request)
        try {
            val response = call.execute()
            val code = response.code()
            if (code != 200 && code != 201) {
                return null
            }
            if (outputJavaFile.exists()) {
                outputJavaFile.delete()
            }
            outputJavaFile.createNewFile()
            var inputStream: InputStream? = null
            try {
                val body = response.body() ?: return null
                inputStream = body.byteStream()
                val buff = ByteArray(1_024 * 4)
                var downloaded: Long = 0
                val target = body.contentLength()
                val output = FileOutputStream(outputJavaFile)
                listener.onDownloadProgress(
                        0L,
                        target
                )
                while (true) {
                    val contentRead = inputStream!!.read(buff)
                    if (contentRead == -1) {
                        break
                    }
                    output.write(buff, 0, contentRead)
                    downloaded += contentRead.toLong()
                    listener.onDownloadProgress(
                            downloaded,
                            target
                    )
                }
                output.flush()
                output.close()
                val succeeded = downloaded == target
                if (!succeeded) {
                    Log.e("jm/debug", "downloaded != target: $downloaded != $target")
                }
                val responseJson = JSONObject()
                responseJson.put("succeeded", succeeded)
                return responseJson.toString()
            } catch (e: IOException) {
                Log.e("jm/debug", "Download error 1/2", e)
                return null
            } finally {
                inputStream?.close()
            }
        } catch (e: IOException) {
            Log.e("jm/debug", "Download error 2/2", e)
            return null
        }
    }

    companion object {

        private val MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8")
    }
}