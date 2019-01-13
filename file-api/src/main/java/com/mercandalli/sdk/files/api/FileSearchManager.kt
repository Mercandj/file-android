package com.mercandalli.sdk.files.api

interface FileSearchManager {

    fun search(
        query: String,
        forceRefresh: Boolean = false
    ): FileSearchResult

    fun getSearchResult(query: String): FileSearchResult

    fun registerFileSearchListener(listener: FileSearchListener)

    fun unregisterFileSearchListener(listener: FileSearchListener)

    interface FileSearchListener {

        fun onFileSearchResultChanged(query: String)
    }
}
