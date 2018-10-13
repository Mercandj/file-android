package com.mercandalli.android.sdk.files.api.online

import android.content.Context
import com.mercandalli.sdk.files.api.*
import com.mercandalli.sdk.files.api.online.FileOnlineLoginRepository
import com.mercandalli.sdk.files.api.online.FileOnlineModule

class FileOnlineAndroidModule(
        private val context: Context,
        private val fileOnlineApiNetwork: FileOnlineApiNetwork
) {

    private val mediaScanner: MediaScanner by lazy {
        MediaScannerOnlineAndroid()
    }

    private val fileOnlineModule by lazy { FileOnlineModule() }
    private val fileOnlineLoginRepository by lazy { createFileOnlineLoginRepository() }
    private val fileOnlineLoginManagerInternal by lazy { createFileOnlineLoginManager() }

    private val fileOnlineApi by lazy {
        createFileOnlineApi()
    }

    fun createFileOnlineManager(): FileManager {
        val fileManager = FileOnlineManagerAndroid(
                fileOnlineApi
        )
        mediaScanner.setListener(object : MediaScanner.RefreshListener {
            override fun onContentChanged(path: String) {
                fileManager.refresh(path)
            }
        })
        return fileManager
    }

    fun createFileOnlineCopyCutManager(): FileCopyCutManager {
        return FileOnlineCopyCutManagerAndroid(
                fileOnlineApi,
                mediaScanner
        )
    }

    fun createFileOnlineCreatorManager(): FileCreatorManager {
        return FileOnlineCreatorManagerAndroid(
                fileOnlineApi,
                mediaScanner
        )
    }

    fun createFileOnlineDeleteManager(): FileDeleteManager {
        return FileOnlineDeleteManagerAndroid(
                fileOnlineApi,
                mediaScanner
        )
    }

    fun getFileOnlineLoginManager() = fileOnlineLoginManagerInternal

    fun createFileOnlineRenameManager(): FileRenameManager {
        return FileOnlineRenameManagerAndroid(
                fileOnlineApi,
                mediaScanner
        )
    }

    fun createFileOnlineSizeManager(): FileSizeManager {
        return FileOnlineSizeManagerAndroid(
                fileOnlineApi
        )
    }

    fun createFileOnlineUploadManager(): FileOnlineUploadManager {
        return FileOnlineUploadManagerImpl(
                fileOnlineApi,
                mediaScanner
        )
    }

    private fun createFileOnlineLoginManager() = fileOnlineModule.createFileOnlineLoginManager(
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

    private fun createFileOnlineApi(): FileOnlineApi {
        return FileOnlineApiImpl(
                fileOnlineApiNetwork,
                fileOnlineLoginManagerInternal
        )
    }
}