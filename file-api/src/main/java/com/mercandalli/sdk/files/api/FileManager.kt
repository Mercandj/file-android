package com.mercandalli.sdk.files.api

interface FileManager {

    fun loadFile(path: String): FileResult

    fun getFile(path: String): FileResult

    fun loadFileChildren(path: String, forceRefresh: Boolean = false): FileChildrenResult

    fun getFileChildren(path: String): FileChildrenResult

    fun registerFileResultListener(listener: FileResultListener)

    fun unregisterFileResultListener(listener: FileResultListener)

    fun registerFileChildrenResultListener(listener: FileChildrenResultListener)

    fun unregisterFileChildrenResultListener(listener: FileChildrenResultListener)

    interface FileResultListener {
        fun onFileResultChanged(path: String)
    }

    interface FileChildrenResultListener {
        fun onFileChildrenResultChanged(path: String)
    }

}