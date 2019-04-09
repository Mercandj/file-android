package com.mercandalli.android.sdk.files.api.internal

import com.mercandalli.sdk.files.api.FileRootManager
import com.mercandalli.sdk.files.api.FileSearchLocal
import com.mercandalli.sdk.files.api.FileSearchManager
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class FileSearchManagerAndroid(
    private val fileRootManager: FileRootManager
) : FileSearchManager {

    private val fileSearchResultMap = HashMap<String, FileSearchResult>()
    private val fileSearchResultListeners = ArrayList<FileSearchManager.FileSearchListener>()

    override fun search(
        query: String,
        forceRefresh: Boolean
    ): FileSearchResult {
        if (fileSearchResultMap.contains(query)) {
            val status = fileSearchResultMap[query]!!.status
            if (status == FileSearchResult.Status.LOADING) {
                return getSearchResult(query)
            }
            if (status == FileSearchResult.Status.LOADED_SUCCEEDED && !forceRefresh) {
                return getSearchResult(query)
            }
        }
        GlobalScope.launch(Dispatchers.Default) {
            val fileSearchResult = loadFileSearchSync(
                query,
                fileRootManager.getFileRootPath()
            )
            GlobalScope.launch(Dispatchers.Main) {
                fileSearchResultMap[query] = fileSearchResult
                for (listener in fileSearchResultListeners) {
                    listener.onFileSearchResultChanged(query)
                }
            }
        }
        return getSearchResult(query)
    }

    override fun getSearchResult(query: String): FileSearchResult {
        if (fileSearchResultMap.contains(query)) {
            return fileSearchResultMap[query]!!
        }
        val fileSearchResultUnloaded = FileSearchResult.createUnloaded(query)
        fileSearchResultMap[query] = fileSearchResultUnloaded
        return fileSearchResultUnloaded
    }

    override fun registerFileSearchListener(listener: FileSearchManager.FileSearchListener) {
        if (fileSearchResultListeners.contains(listener)) {
            return
        }
        fileSearchResultListeners.add(listener)
    }

    override fun unregisterFileSearchListener(listener: FileSearchManager.FileSearchListener) {
        fileSearchResultListeners.remove(listener)
    }

    companion object {

        @JvmStatic
        private fun loadFileSearchSync(
            query: String,
            path: String
        ): FileSearchResult {
            val paths = FileSearchLocal.searchSync(
                query,
                path
            )
            val files = ArrayList<File>()
            for (currentPath in paths) {
                val ioFile = java.io.File(currentPath)
                val file = File.create(ioFile)
                files.add(file)
            }
            return FileSearchResult.createLoaded(query, files)
        }
    }
}
