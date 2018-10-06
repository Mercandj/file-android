package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileManager

internal class FileOnlineManagerAndroid(
        private val fileOnlineApi: FileOnlineApi
) : FileManager {

    override fun loadFileChildren(path: String, forceRefresh: Boolean): FileChildrenResult {
        return FileChildrenResult.createUnloaded(path)
    }

    override fun getFileChildren(path: String): FileChildrenResult {
        return FileChildrenResult.createUnloaded(path)
    }

    override fun registerFileChildrenResultListener(listener: FileManager.FileChildrenResultListener) {

    }

    override fun unregisterFileChildrenResultListener(listener: FileManager.FileChildrenResultListener) {

    }
}