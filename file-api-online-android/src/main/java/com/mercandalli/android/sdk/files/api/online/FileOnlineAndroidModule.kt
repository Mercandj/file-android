package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.online.FileOnlineLoginManager
import com.mercandalli.sdk.files.api.online.FileOnlineModule

class FileOnlineAndroidModule(
        private val fileOnlineApiNetwork: FileOnlineApiNetwork
) {

    private val fileOnlineModule by lazy { FileOnlineModule() }

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

    fun createFileOnlineLoginManager() = fileOnlineModule.createFileOnlineLoginManager()

    private fun createFileOnlineApi(
            fileOnlineLoginManager: FileOnlineLoginManager
    ): FileOnlineApi {
        return FileOnlineApiImpl(
                fileOnlineApiNetwork,
                fileOnlineLoginManager
        )
    }
}