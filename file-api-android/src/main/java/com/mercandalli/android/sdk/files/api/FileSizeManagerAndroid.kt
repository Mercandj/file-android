package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.FileSizeManager
import com.mercandalli.sdk.files.api.FileSizeResult
import com.mercandalli.sdk.files.api.FileSizeUtils
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
internal class FileSizeManagerAndroid(
        private val permissionManager: PermissionManager
) : FileSizeManager {

    private val fileSizeResultMap = HashMap<String, FileSizeResult>()
    private val fileSizeResultListeners = ArrayList<FileSizeManager.FileSizeResultListener>()

    override fun loadSize(path: String, forceRefresh: Boolean): FileSizeResult {
        if (fileSizeResultMap.contains(path)) {
            val status = fileSizeResultMap[path]!!.status
            if (status == FileSizeResult.Status.LOADING) {
                return getSize(path)
            }
            if (status == FileSizeResult.Status.LOADED_SUCCEEDED && !forceRefresh) {
                return getSize(path)
            }
        }
        if (permissionManager.shouldRequestStoragePermission()) {
            return getSize(path)
        }
        fileSizeResultMap[path] = FileSizeResult.createLoading(path)
        GlobalScope.launch(Dispatchers.Default) {
            val fileChildrenResult = FileSizeUtils.computeSizeFromJavaFileSync(path)
            GlobalScope.launch(Dispatchers.Main) {
                fileSizeResultMap[path] = fileChildrenResult
                for (listener in fileSizeResultListeners) {
                    listener.onFileSizeResultChanged(path)
                }
            }
        }
        return getSize(path)
    }

    override fun getSize(path: String): FileSizeResult {
        if (fileSizeResultMap.contains(path)) {
            return fileSizeResultMap[path]!!
        }
        val fileSizeResultUnloaded = FileSizeResult.createUnloaded(path)
        fileSizeResultMap[path] = fileSizeResultUnloaded
        return fileSizeResultUnloaded
    }

    override fun registerFileSizeResultListener(listener: FileSizeManager.FileSizeResultListener) {
        if (fileSizeResultListeners.contains(listener)) {
            return
        }
        fileSizeResultListeners.add(listener)
    }

    override fun unregisterFileSizeResultListener(listener: FileSizeManager.FileSizeResultListener) {
        fileSizeResultListeners.remove(listener)
    }
}