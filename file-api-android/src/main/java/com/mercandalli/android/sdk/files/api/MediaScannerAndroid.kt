package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.MediaScanner

internal class MediaScannerAndroid(
        private val addOn: AddOn
) : MediaScanner {

    private var refreshListeners= ArrayList<MediaScanner.RefreshListener>()

    override fun refresh(path: String) {
        addOn.refreshSystemMediaScanDataBase(path)
        for (listener in refreshListeners) {
            listener.onContentChanged(path)
        }
    }

    override fun addListener(listener: MediaScanner.RefreshListener) {
        if (refreshListeners.contains(listener)) {
            return
        }
        refreshListeners.add(listener)
    }

    interface AddOn {

        /**
         * @param docPath : absolute path of file for which broadcast will be send to refresh
         * media database
         */
        fun refreshSystemMediaScanDataBase(path: String)
    }
}