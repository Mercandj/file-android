package com.mercandalli.sdk.files.api

interface FileSearchManager {

    fun search(query: String)

    fun getSearchResult(query: String): FileSearchResult

    fun registerFileSearchListener(listener: FileSearchListener)

    fun unregisterFileSearchListener(listener: FileSearchListener)

    interface FileSearchListener {

        fun onFileSearchResultChanged(path: String)
    }
}
