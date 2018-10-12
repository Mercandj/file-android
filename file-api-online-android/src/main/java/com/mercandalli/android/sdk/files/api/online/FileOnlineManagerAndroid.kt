package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileManager
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch

internal class FileOnlineManagerAndroid(
        private val fileOnlineApi: FileOnlineApi
) : FileManager {

    private val fileChildrenResultMap = HashMap<String, FileChildrenResult>()
    private val fileChildrenResultListeners = ArrayList<FileManager.FileChildrenResultListener>()

    override fun loadFileChildren(path: String, forceRefresh: Boolean): FileChildrenResult {
        if (fileChildrenResultMap.contains(path)) {
            val status = fileChildrenResultMap[path]!!.status
            if (status == FileChildrenResult.Status.LOADING) {
                return getFileChildren(path)
            }
            if (status == FileChildrenResult.Status.LOADED_SUCCEEDED && !forceRefresh) {
                return getFileChildren(path)
            }
        }
        fileChildrenResultMap[path] = FileChildrenResult.createLoading(path)
        GlobalScope.launch(Dispatchers.Default) {
            val fileChildrenResult = loadFileChildrenSync(path)
            GlobalScope.launch(Dispatchers.Main) {
                fileChildrenResultMap[path] = fileChildrenResult
                for (listener in fileChildrenResultListeners) {
                    listener.onFileChildrenResultChanged(path)
                }
            }
        }
        return getFileChildren(path)
    }

    override fun getFileChildren(path: String): FileChildrenResult {
        if (fileChildrenResultMap.contains(path)) {
            return fileChildrenResultMap[path]!!
        }
        val fileChildrenResultUnloaded = FileChildrenResult.createUnloaded(path)
        fileChildrenResultMap[path] = fileChildrenResultUnloaded
        return fileChildrenResultUnloaded
    }

    override fun registerFileChildrenResultListener(listener: FileManager.FileChildrenResultListener) {
        if (fileChildrenResultListeners.contains(listener)) {
            return
        }
        fileChildrenResultListeners.add(listener)
    }

    override fun unregisterFileChildrenResultListener(listener: FileManager.FileChildrenResultListener) {
        fileChildrenResultListeners.remove(listener)
    }

    fun refresh(path: String) {
        if (!fileChildrenResultMap.containsKey(path)) {
            return
        }
        loadFileChildren(path, true)
    }

    private fun loadFileChildrenSync(path: String): FileChildrenResult {
        val serverResponseFiles = fileOnlineApi.getFromParent(path)
        return FileChildrenResult.createLoaded(path, serverResponseFiles!!.files)
    }
}