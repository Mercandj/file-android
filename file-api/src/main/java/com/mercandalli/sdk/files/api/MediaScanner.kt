package com.mercandalli.sdk.files.api

interface MediaScanner {

    fun refresh(path: String)

    fun setListener(listener: RefreshListener)

    interface RefreshListener {

        fun onContentChanged(path: String)
    }
}