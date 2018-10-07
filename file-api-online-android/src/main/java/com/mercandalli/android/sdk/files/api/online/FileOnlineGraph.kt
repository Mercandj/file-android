package com.mercandalli.android.sdk.files.api.online

class FileOnlineGraph(
        private val fileOnlineModule: FileOnlineAndroidModule
) {

    private val fileOnlineManager by lazy {
        fileOnlineModule.createFileOnlineManager(fileOnlineLoginManager)
    }

    private val fileOnlineLoginManager by lazy {
        fileOnlineModule.createFileOnlineLoginManager()
    }

    companion object {

        private lateinit var graph: FileOnlineGraph

        fun init(
                fileOnlineApiNetwork: FileOnlineApiNetwork
        ) {
            val fileOnlineModule = FileOnlineAndroidModule(
                    fileOnlineApiNetwork
            )
            graph = FileOnlineGraph(
                    fileOnlineModule
            )
        }

        fun getFileOnlineManager() = graph.fileOnlineManager
    }
}