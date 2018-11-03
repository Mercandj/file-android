package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.MediaScanner

internal class MediaScannerOnlineAndroid : MediaScanner {

    private var refreshListeners = ArrayList<MediaScanner.RefreshListener>()

    override fun refresh(path: String) {
        val cleanedPath = File.cleanPath(path)
        for (listener in refreshListeners) {
            listener.onContentChanged(cleanedPath)
        }
    }

    override fun addListener(listener: MediaScanner.RefreshListener) {
        if (refreshListeners.contains(listener)) {
            return
        }
        refreshListeners.add(listener)
    }
}
