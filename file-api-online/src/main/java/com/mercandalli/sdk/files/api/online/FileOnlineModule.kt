package com.mercandalli.sdk.files.api.online

class FileOnlineModule {

    fun createFileOnlineLoginManager(): FileOnlineLoginManager {
        return FileOnlineLoginManagerImpl()
    }
}