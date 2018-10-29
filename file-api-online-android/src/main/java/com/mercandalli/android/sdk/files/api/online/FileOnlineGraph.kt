package com.mercandalli.android.sdk.files.api.online

import android.content.Context
import com.mercandalli.sdk.files.api.MediaScanner

class FileOnlineGraph(
        private val fileOnlineModule: FileOnlineAndroidModule
) {

    private val fileOnlineManager by lazy { fileOnlineModule.createFileOnlineManager() }
    private val fileOnlineCopyCutManager by lazy { fileOnlineModule.createFileOnlineCopyCutManager() }
    private val fileOnlineCreatorManager by lazy { fileOnlineModule.createFileOnlineCreatorManager() }
    private val fileOnlineDeleteManager by lazy { fileOnlineModule.createFileOnlineDeleteManager() }
    private val fileOnlineDownloadManager by lazy { fileOnlineModule.createFileOnlineDownloadManager() }
    private val fileOnlineLoginManager by lazy { fileOnlineModule.getFileOnlineLoginManager() }
    private val fileOnlineRenameManager by lazy { fileOnlineModule.createFileOnlineRenameManager() }
    private val fileOnlineSizeManager by lazy { fileOnlineModule.createFileOnlineSizeManager() }
    private val fileOnlineUploadManager by lazy { fileOnlineModule.createFileOnlineUploadManager() }

    companion object {

        private lateinit var graph: FileOnlineGraph

        fun getFileOnlineManager() = graph.fileOnlineManager
        fun getFileOnlineCopyCutManager() = graph.fileOnlineCopyCutManager
        fun getFileOnlineCreatorManager() = graph.fileOnlineCreatorManager
        fun getFileOnlineDeleteManager() = graph.fileOnlineDeleteManager
        fun getFileOnlineDownloadManager() = graph.fileOnlineDownloadManager
        fun getFileOnlineLoginManager() = graph.fileOnlineLoginManager
        fun getFileOnlineRenameManager() = graph.fileOnlineRenameManager
        fun getFileOnlineSizeManager() = graph.fileOnlineSizeManager
        fun getFileOnlineUploadManager() = graph.fileOnlineUploadManager

        fun init(
                context: Context,
                fileOnlineApiNetwork: FileOnlineApiNetwork,
                localMediaScanner: MediaScanner
        ) {
            val fileOnlineModule = FileOnlineAndroidModule(
                    context,
                    fileOnlineApiNetwork,
                    localMediaScanner
            )
            graph = FileOnlineGraph(
                    fileOnlineModule
            )
        }
    }
}