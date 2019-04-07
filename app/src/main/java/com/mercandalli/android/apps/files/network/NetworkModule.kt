package com.mercandalli.android.apps.files.network

import okhttp3.OkHttpClient
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

    private val networkDownloader: NetworkDownloader by lazy {
        NetworkDownloaderImpl(okHttpClient)
    }

    private val networkUploader: NetworkUploader by lazy {
        NetworkUploaderImpl(okHttpClient)
    }

    fun createOkHttpClientLazy(): Lazy<OkHttpClient> = okHttpClient

    fun createNetworkManager(): NetworkManager {
        return NetworkManagerImpl(
            networkDownloader,
            networkUploader,
            okHttpClient
        )
    }
}
