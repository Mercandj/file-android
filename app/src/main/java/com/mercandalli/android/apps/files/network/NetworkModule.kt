package com.mercandalli.android.apps.files.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Headers
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.RequestBody
import okhttp3.Request
import okhttp3.MediaType
import org.json.JSONObject
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.net.SocketTimeoutException

class NetworkModule {

    private val okHttpClient = lazy {
        val builder = OkHttpClient.Builder()
        // if (BuildConfig.DEBUG) {
        //     HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //     interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //     builder.addInterceptor(interceptor);
        // }
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.build()
    }

    private val networkDownloader: NetworkDownloader by lazy {
        NetworkDownloaderImpl(okHttpClient)
    }

    private val networkUploader: NetworkUploader by lazy {
        NetworkUploaderImpl(okHttpClient)
    }

    fun createOkHttpClientLazy(): Lazy<OkHttpClient> = okHttpClient

    fun createNetwork() = object : NetworkManager {

        override fun getSync(
            url: String,
            headers: Map<String, String>
        ): String? {
            val request = Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .build()
            return call(request)
        }

        override fun postSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject
        ): String? {
            val body = RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString())
            val request = Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(body)
                .build()
            return call(request)
        }

        override fun postDownloadSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject,
            javaFile: File,
            listener: NetworkManager.DownloadProgressListener
        ) = networkDownloader.postDownloadSync(
            url,
            headers,
            jsonObject,
            javaFile,
            listener
        )

        override fun postUploadSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject,
            javaFile: java.io.File,
            listener: NetworkManager.UploadProgressListener
        ) = networkUploader.postUploadSync(
            url,
            headers,
            jsonObject,
            javaFile,
            listener
        )

        override fun deleteSync(
            url: String,
            headers: Map<String, String>,
            jsonObject: JSONObject
        ): String? {
            val body = RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString())
            val request = Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .delete(body)
                .build()
            return call(request)
        }

        private fun call(request: Request): String? {
            var response: Response? = null
            var body: ResponseBody? = null
            try {
                response = okHttpClient.value.newCall(request).execute()
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
    }

    companion object {

        private val MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8")

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
