package com.mercandalli.android.sdk.files.api.online

import android.util.Log
import okhttp3.*
import java.io.Closeable
import java.io.IOException
import java.util.concurrent.TimeUnit

class FileOnlineApiNetworkAndroidTest {

    companion object {

        fun createFileOnlineApiNetwork(): FileOnlineApiNetwork {
            val okHttpClient = createOkHttpClient()
            return createFileOnlineApiNetwork(okHttpClient)
        }

        private fun createFileOnlineApiNetwork(okHttpClient: OkHttpClient) = object : FileOnlineApiNetwork {
            override fun getSync(url: String, headers: Map<String, String>): String? {
                val request = Request.Builder()
                        .url(url)
                        .headers(Headers.of(headers))
                        .build()
                var response: Response? = null
                var body: ResponseBody? = null
                try {
                    response = okHttpClient.newCall(request).execute()
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

        private fun createOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(15, TimeUnit.SECONDS)
            return builder.build()
        }
    }
}