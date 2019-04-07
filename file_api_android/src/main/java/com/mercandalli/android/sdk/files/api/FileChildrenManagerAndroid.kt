package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileChildrenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class FileChildrenManagerAndroid(
    private val permissionManager: PermissionManager
) : FileChildrenManager {

    private val fileChildrenResultMap = HashMap<String, FileChildrenResult>()
    private val fileChildrenResultListeners = ArrayList<FileChildrenManager.FileChildrenResultListener>()

    override fun loadFileChildren(
        path: String,
        forceRefresh: Boolean
    ): FileChildrenResult {
        if (fileChildrenResultMap.contains(path)) {
            val status = fileChildrenResultMap[path]!!.status
            if (status == FileChildrenResult.Status.LOADING) {
                return getFileChildren(path)
            }
            if (status == FileChildrenResult.Status.LOADED_SUCCEEDED && !forceRefresh) {
                return getFileChildren(path)
            }
        }
        if (permissionManager.shouldRequestStoragePermission()) {
            return getFileChildren(path)
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

    override fun registerFileChildrenResultListener(listener: FileChildrenManager.FileChildrenResultListener) {
        if (fileChildrenResultListeners.contains(listener)) {
            return
        }
        fileChildrenResultListeners.add(listener)
    }

    override fun unregisterFileChildrenResultListener(listener: FileChildrenManager.FileChildrenResultListener) {
        fileChildrenResultListeners.remove(listener)
    }

    fun refresh(path: String) {
        if (!fileChildrenResultMap.containsKey(path)) {
            return
        }
        loadFileChildren(path, true)
    }

    companion object {

        @JvmStatic
        private fun loadFileChildrenSync(path: String): FileChildrenResult {
            val ioFile = java.io.File(path)
            if (!ioFile.isDirectory) {
                return FileChildrenResult.createErrorNotFolder(path)
            }
            val ioFiles = ioFile.listFiles()
            val files = ArrayList<File>()
            if (ioFiles != null) {
                for (ioFileLoop in ioFiles) {
                    val file = File.create(ioFileLoop)
                    files.add(file)
                }
            }
            return FileChildrenResult.createLoaded(path, files)
        }
    }
}
