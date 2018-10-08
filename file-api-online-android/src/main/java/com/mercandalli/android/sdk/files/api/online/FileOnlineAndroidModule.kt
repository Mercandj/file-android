package com.mercandalli.android.sdk.files.api.online

import android.content.Context
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.online.FileOnlineLoginManager
import com.mercandalli.sdk.files.api.online.FileOnlineLoginRepository
import com.mercandalli.sdk.files.api.online.FileOnlineModule

class FileOnlineAndroidModule(
        private val context: Context,
        private val fileOnlineApiNetwork: FileOnlineApiNetwork
) {

    private val fileOnlineModule by lazy { FileOnlineModule() }
    private val fileOnlineLoginRepository by lazy { createFileOnlineLoginRepository() }

    fun createFileOnlineManager(
            fileOnlineLoginManager: FileOnlineLoginManager
    ): FileManager {
        val fileOnlineApi = createFileOnlineApi(
                fileOnlineLoginManager
        )
        return FileOnlineManagerAndroid(
                fileOnlineApi
        )
    }

    fun createFileOnlineLoginManager() = fileOnlineModule.createFileOnlineLoginManager(
            fileOnlineLoginRepository
    )

    private fun createFileOnlineLoginRepository(): FileOnlineLoginRepository {
        val sharedPreferences = context.getSharedPreferences(
                FileOnlineLoginRepositoryImpl.PREFERENCE_NAME,
                Context.MODE_PRIVATE
        )
        return FileOnlineLoginRepositoryImpl(
                sharedPreferences
        )
    }

    private fun createFileOnlineApi(
            fileOnlineLoginManager: FileOnlineLoginManager
    ): FileOnlineApi {
        return FileOnlineApiImpl(
                fileOnlineApiNetwork,
                fileOnlineLoginManager
        )
    }
}