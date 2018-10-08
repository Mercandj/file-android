package com.mercandalli.sdk.files.api.online

class FileOnlineModule {

    fun createFileOnlineLoginManager(
            fileOnlineLoginRepository: FileOnlineLoginRepository? = null
    ): FileOnlineLoginManager {
        return FileOnlineLoginManagerImpl(
                fileOnlineLoginRepository
        )
    }
}