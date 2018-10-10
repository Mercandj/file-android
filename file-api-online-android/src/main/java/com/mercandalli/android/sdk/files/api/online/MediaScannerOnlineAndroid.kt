package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.MediaScanner

internal class MediaScannerOnlineAndroid: MediaScanner {

    private var refreshListener: MediaScanner.RefreshListener? = null

    override fun refresh(path: String) {
        refreshListener?.onContentChanged(path)
    }

    override fun setListener(listener: MediaScanner.RefreshListener) {
        refreshListener = listener
    }
}