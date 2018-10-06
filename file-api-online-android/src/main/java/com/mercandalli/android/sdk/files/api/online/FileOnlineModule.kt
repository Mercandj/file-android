package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.FileManager

class FileOnlineModule(
        private val fileOnlineApiNetwork: FileOnlineApiNetwork
        ) {

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

    fun createFileOnlineLoginManager(): FileOnlineLoginManager {
        return FileOnlineLoginManagerImpl()
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