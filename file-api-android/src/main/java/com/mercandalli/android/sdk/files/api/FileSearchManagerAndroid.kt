package com.mercandalli.android.sdk.files.api

import androidx.annotation.VisibleForTesting
import com.mercandalli.sdk.files.api.FileSearchManager
import com.mercandalli.sdk.files.api.FileSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FileSearchManagerAndroid(
    private val externalStorageDirectoryAbsolutePath: String
) : FileSearchManager {

    private val fileSearchResultMap = HashMap<String, FileSearchResult>()
    private val fileSearchResultListeners = ArrayList<FileSearchManager.FileSearchListener>()

    override fun search(query: String) {
        // TODO
        GlobalScope.launch(Dispatchers.Default) {
            val paths = searchSync(
                query,
                externalStorageDirectoryAbsolutePath
            )
            GlobalScope.launch(Dispatchers.Main) {
            }
        }
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

        @VisibleForTesting
        fun searchSync(
            query: String,
            path: String
        ): List<String> {
            val pathsResult = ArrayList<String>()
            val ioFile = java.io.File(path)
            if (!ioFile.isDirectory) {
                val contains = ioFile.name.toLowerCase().contains(query.toLowerCase())
                if (contains && !pathsResult.contains(path)) {
                    pathsResult.add(path)
                }
                return pathsResult
            }
            val ioFiles = ioFile.listFiles() ?: return pathsResult
            for (ioFileLoop in ioFiles) {
                val contains = ioFileLoop.name.toLowerCase().contains(query.toLowerCase())
                if (contains && !pathsResult.contains(ioFileLoop.absolutePath)) {
                    pathsResult.add(ioFileLoop.absolutePath)
                }
                pathsResult.addAll(searchSync(query, ioFileLoop.absolutePath))
            }
            return pathsResult
        }
    }
}
