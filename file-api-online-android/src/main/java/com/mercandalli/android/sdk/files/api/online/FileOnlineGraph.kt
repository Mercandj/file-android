package com.mercandalli.android.sdk.files.api.online

import android.content.Context

class FileOnlineGraph(
        private val fileOnlineModule: FileOnlineAndroidModule
) {

    private val fileOnlineManager by lazy { fileOnlineModule.createFileOnlineManager() }
    private val fileOnlineDeleteManager by lazy { fileOnlineModule.createFileOnlineDeleteManager() }
    private val fileOnlineLoginManager by lazy { fileOnlineModule.getFileOnlineLoginManager() }
    private val fileOnlineRenameManager by lazy { fileOnlineModule.createFileOnlineRenameManager() }
    private val fileOnlineUploadManager by lazy { fileOnlineModule.createFileOnlineUploadManager() }

    companion object {

        private lateinit var graph: FileOnlineGraph

        fun getFileOnlineManager() = graph.fileOnlineManager
        fun getFileOnlineDeleteManager() = graph.fileOnlineDeleteManager
        fun getFileOnlineLoginManager() = graph.fileOnlineLoginManager
        fun getFileOnlineRenameManager() = graph.fileOnlineRenameManager
        fun getFileOnlineUploadManager() = graph.fileOnlineUploadManager

        fun init(
                context: Context,
                fileOnlineApiNetwork: FileOnlineApiNetwork
        ) {
            val fileOnlineModule = FileOnlineAndroidModule(
                    context,
                    fileOnlineApiNetwork
            )
            graph = FileOnlineGraph(
                    fileOnlineModule
            )
        }
    }
}