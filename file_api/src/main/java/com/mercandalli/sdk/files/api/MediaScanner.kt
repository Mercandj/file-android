package com.mercandalli.sdk.files.api

interface MediaScanner {

    fun refresh(path: String)

    fun addListener(listener: RefreshListener)

    interface RefreshListener {

        fun onContentChanged(path: String)
    }
}
