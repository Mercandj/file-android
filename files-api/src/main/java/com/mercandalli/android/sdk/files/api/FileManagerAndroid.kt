package com.mercandalli.android.sdk.files.api

class FileManagerAndroid : FileManager {

    private val fileResultMap = HashMap<String, FileResult>()

    override fun loadFile(path: String): FileResult {
        fileResultMap[path] = FileResult.createLoading(path)
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

    override fun loadFileChildren(path: String): FileChildrenResult {
        return getFileChildren(path)
    }

    override fun getFileChildren(path: String): FileChildrenResult {
        return FileChildrenResult.createUnloaded(path)
    }

    override fun registerFileResultListener(listener: FileManager.FileResultListener) {

    }

    override fun unregisterFileResultListener(listener: FileManager.FileResultListener) {

    }

    override fun registerFileChildrenResultListener(listener: FileManager.FileChildrenResultListener) {

    }

    override fun unregisterFileChildrenResultListener(listener: FileManager.FileChildrenResultListener) {

    }

}