package com.mercandalli.sdk.files.api.online

import java.util.Date

class FileOnlineModule {

    private val fileOnlineTokenCreator: FileOnlineTokenCreator by lazy {
        val addOn = object : FileOnlineTokenCreatorImpl.AddOn {
            override fun getCurrentDate() = Date()
        }
        FileOnlineTokenCreatorImpl(addOn)
    }

    fun createFileOnlineLoginManager(
        fileOnlineLoginRepository: FileOnlineLoginRepository? = null
    ): FileOnlineLoginManager {
        return FileOnlineLoginManagerImpl(
            fileOnlineLoginRepository,
            fileOnlineTokenCreator
        )
    }
}
