package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.FileResult
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class FileManagerAndroid(
        private val permissionManager: PermissionManager
) : FileManager {

    private val fileResultMap = HashMap<String, FileResult>()
    private val fileChildrenResultMap = HashMap<String, FileChildrenResult>()
    private val fileResultListeners = ArrayList<FileManager.FileResultListener>()
    private val fileChildrenResultListeners = ArrayList<FileManager.FileChildrenResultListener>()

    override fun loadFile(path: String): FileResult {
        if (fileResultMap.contains(path)) {
            val status = fileResultMap[path]!!.status
            if (status == FileResult.LOADING || status == FileResult.LOADED) {
                return getFile(path)
            }
        }
        if (permissionManager.shouldRequestStoragePermission()) {
            return getFile(path)
        }
        fileResultMap[path] = FileResult.createLoading(path)
        launch(CommonPool) {
            val fileResult = loadFileSync(path)
            launch(UI) {
                fileResultMap[path] = fileResult
                for (listener in fileResultListeners) {
                    listener.onFileResultChanged(path)
                }
            }
        }
        return getFile(path)
    }

    override fun getFile(path: String): FileResult {
        if (fileResultMap.contains(path)) {
            return fileResultMap[path]!!
        }
        val fileResultUnloaded = FileResult.createUnloaded(path)
        fileResultMap[path] = fileResultUnloaded
        return fileResultUnloaded
    }

    override fun loadFileChildren(path: String, forceRefresh: Boolean): FileChildrenResult {
        if (fileChildrenResultMap.contains(path)) {
            val status = fileChildrenResultMap[path]!!.status
            if (status == FileChildrenResult.LOADING) {
                return getFileChildren(path)
            }
            if (status == FileChildrenResult.LOADED_SUCCEEDED && !forceRefresh) {
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

    override fun registerFileResultListener(listener: FileManager.FileResultListener) {
        if (fileResultListeners.contains(listener)) {
            return
        }
        fileResultListeners.add(listener)
    }

    override fun unregisterFileResultListener(listener: FileManager.FileResultListener) {
        fileResultListeners.remove(listener)
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

    companion object {

        @JvmStatic
        private fun loadFileSync(path: String): FileResult {
            val ioFile = java.io.File(path)
            val file = convertToFile(ioFile)
            return FileResult.createLoaded(path, file)
        }

        @JvmStatic
        private fun loadFileChildrenSync(path: String): FileChildrenResult {
            val ioFile = java.io.File(path)
            if (!ioFile.isDirectory) {
                return FileChildrenResult.createErrorNotFolder(path)
            }
            val iosFiles = ioFile.listFiles()
            val files = ArrayList<File>()
            for (iosFile in iosFiles) {
                val file = convertToFile(ioFile)
                files.add(file)
            }
            return FileChildrenResult.createLoaded(path, files)
        }

        @JvmStatic
        private fun convertToFile(ioFile: java.io.File): File {
            val path = ioFile.absolutePath
            val directory = ioFile.isDirectory
            val parentPath = parentPath(ioFile)
            return File(
                    path,
                    parentPath,
                    directory)
        }

        @JvmStatic
        private fun parentPath(ioFile: java.io.File): String? {
            val ioParentPath = ioFile.parent
            return if (ioParentPath == "") null else ioParentPath
        }

    }

}