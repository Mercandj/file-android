package com.mercandalli.android.sdk.files.api.online

import android.content.Context
import com.mercandalli.sdk.files.api.MediaScanner
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileCreatorManager
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileRenameManager
import com.mercandalli.sdk.files.api.FileSizeManager
import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.online.FileOnlineLoginRepository
import com.mercandalli.sdk.files.api.online.FileOnlineModule

class FileOnlineAndroidModule(
    private val context: Context,
    private val fileOnlineApiNetwork: FileOnlineApiNetwork,
    private val localMediaScanner: MediaScanner
) {

    private val onlineMediaScanner: MediaScanner by lazy {
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
        onlineMediaScanner.addListener(object : MediaScanner.RefreshListener {
            override fun onContentChanged(path: String) {
                fileManager.refresh(path)
            }
        })
        return fileManager
    }

    fun createFileOnlineChildrenManager(): FileChildrenManager {
        val fileManager = FileOnlineChildrenManagerAndroid(
            fileOnlineApi
        )
        onlineMediaScanner.addListener(object : MediaScanner.RefreshListener {
            override fun onContentChanged(path: String) {
                fileManager.refresh(path)
            }
        })
        return fileManager
    }

    fun createFileOnlineCopyCutManager(): FileCopyCutManager {
        return FileOnlineCopyCutManagerAndroid(
            fileOnlineApi,
            onlineMediaScanner
        )
    }

    fun createFileOnlineCreatorManager(): FileCreatorManager {
        return FileOnlineCreatorManagerAndroid(
            fileOnlineApi,
            onlineMediaScanner
        )
    }

    fun createFileOnlineDeleteManager(): FileDeleteManager {
        return FileOnlineDeleteManagerAndroid(
            fileOnlineApi,
            onlineMediaScanner
        )
    }

    fun createFileOnlineDownloadManager(): FileOnlineDownloadManager {
        return FileOnlineDownloadManagerAndroid(
            fileOnlineApi,
            localMediaScanner
        )
    }

    fun getFileOnlineLoginManager() = fileOnlineLoginManagerInternal

    fun createFileOnlineRenameManager(): FileRenameManager {
        return FileOnlineRenameManagerAndroid(
            fileOnlineApi,
            onlineMediaScanner
        )
    }

    fun createFileOnlineSizeManager(): FileSizeManager {
        val fileSizeManager = FileOnlineSizeManagerAndroid(
            fileOnlineApi
        )
        onlineMediaScanner.addListener(object : MediaScanner.RefreshListener {
            override fun onContentChanged(path: String) {
                fileSizeManager.loadSize(path, true)
            }
        })
        return fileSizeManager
    }

    fun createFileOnlineUploadManager(): FileOnlineUploadManager {
        return FileOnlineUploadManagerAndroid(
            fileOnlineApi,
            onlineMediaScanner
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
