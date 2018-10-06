package com.mercandalli.android.apps.files.network

import android.util.Log
import okhttp3.*
import java.io.Closeable
import java.io.IOException
import java.util.concurrent.TimeUnit

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

    fun createOkHttpClientLazy(): Lazy<OkHttpClient> = okHttpClient

    fun createNetwork() = object : Network {
        override fun requestSync(url: String, headers: Map<String, String>): String? {
            val request = Request.Builder()
                    .url(url)
                    .headers(Headers.of(headers))
                    .build()
            var response: Response? = null
            var body: ResponseBody? = null
            try {
                response = okHttpClient.value.newCall(request).execute()
                body = response!!.body()
                return body!!.string()
            } catch (e: IOException) {
                Log.e("jm/debug", "", e)
            } finally {
                closeSilently(body, response)
            }
            return null
        }
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
