package com.mercandalli.android.sdk.files.api.internal

import com.mercandalli.android.sdk.files.api.PermissionManager
import com.mercandalli.sdk.files.api.FileSizeManager
import com.mercandalli.sdk.files.api.FileSizeResult
import com.mercandalli.sdk.files.api.FileSizeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class FileSizeManagerAndroid(
    private val permissionManager: PermissionManager
) : FileSizeManager {

    private val fileSizeResultMap = HashMap<String, FileSizeResult>()
    private val fileSizeResultListeners = ArrayList<FileSizeManager.FileSizeResultListener>()

    override fun loadSize(
        path: String,
        forceRefresh: Boolean
    ): FileSizeResult {
        if (fileSizeResultMap.contains(path)) {
            val status = fileSizeResultMap[path]!!.status
            if (status == FileSizeResult.Status.LOADING) {
                return getSize(path)
            }
            if (status == FileSizeResult.Status.LOADED_SUCCEEDED && !forceRefresh) {
                return getSize(path)
            }
        }
        if (permissionManager.requestStoragePermissionIfRequired()) {
            return getSize(path)
        }
        fileSizeResultMap[path] = FileSizeResult.createLoading(path)
        GlobalScope.launch(Dispatchers.Default) {
            val fileChildrenResult = computeSizeSync(path)
            GlobalScope.launch(Dispatchers.Main) {
                fileSizeResultMap[path] = fileChildrenResult
                for (listener in fileSizeResultListeners) {
                    listener.onFileSizeResultChanged(path)
                }
            }
        }
        return getSize(path)
    }

    override fun setSize(
        path: String,
        size: Long
    ) {
        fileSizeResultMap[path] = FileSizeResult.createLoaded(
            path,
            size
        )
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

    private fun computeSizeSync(
        path: String
    ): FileSizeResult {
        return FileSizeUtils.computeSizeFromJavaFileSync(
            path
        )
    }
}
