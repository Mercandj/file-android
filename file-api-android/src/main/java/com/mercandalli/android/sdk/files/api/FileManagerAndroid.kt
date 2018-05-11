package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileManager
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class FileManagerAndroid(
        private val permissionManager: PermissionManager
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
        if (permissionManager.shouldRequestStoragePermission()) {
            return getFileChildren(path)
        }
        fileChildrenResultMap[path] = FileChildrenResult.createLoading(path)
        launch(CommonPool) {
            val fileChildrenResult = loadFileChildrenSync(path)
            launch(UI) {
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

    companion object {

        @JvmStatic
        private fun loadFileChildrenSync(path: String): FileChildrenResult {
            val ioFile = java.io.File(path)
            if (!ioFile.isDirectory) {
                return FileChildrenResult.createErrorNotFolder(path)
            }
            val iosFiles = ioFile.listFiles()
            val files = ArrayList<File>()
            for (ioFileLoop in iosFiles) {
                val file = convertToFile(ioFileLoop)
                files.add(file)
            }
            return FileChildrenResult.createLoaded(path, files)
        }

        @JvmStatic
        private fun convertToFile(ioFile: java.io.File): File {
            val id = ioFile.absolutePath
            val path = ioFile.absolutePath
            val name = ioFile.name
            val directory = ioFile.isDirectory
            val parentPath = parentPath(ioFile)
            val length = ioFile.length()
            val lastModified = ioFile.lastModified()
            return File(
                    id,
                    path,
                    parentPath,
                    directory,
                    name,
                    length,
                    lastModified)
        }

        @JvmStatic
        private fun parentPath(ioFile: java.io.File): String? {
            val ioParentPath = ioFile.parent
            return if (ioParentPath == "") null else ioParentPath
        }

    }

}