package com.mercandalli.android.sdk.files.api.online

import android.content.Context

class FileOnlineGraph(
        private val fileOnlineModule: FileOnlineAndroidModule
) {

    private val fileOnlineManager by lazy { fileOnlineModule.createFileOnlineManager(fileOnlineLoginManager) }
    private val fileOnlineLoginManager by lazy { fileOnlineModule.createFileOnlineLoginManager() }
    private val fileOnlineUploadManager by lazy { fileOnlineModule.createFileOnlineUploadManager(fileOnlineLoginManager) }

    companion object {

        private lateinit var graph: FileOnlineGraph

        fun getFileOnlineManager() = graph.fileOnlineManager
        fun getFileOnlineLoginManager() = graph.fileOnlineLoginManager
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