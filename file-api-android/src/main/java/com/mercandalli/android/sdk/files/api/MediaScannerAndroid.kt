package com.mercandalli.android.sdk.files.api

class MediaScannerAndroid(
        private val addOn: AddOn
) : MediaScanner {

    private var refreshListener: MediaScanner.RefreshListener? = null

    override fun refresh(path: String) {
        addOn.refreshSystemMediaScanDataBase(path)
        refreshListener?.onContentChanged(path)
    }

    override fun setListener(listener: MediaScanner.RefreshListener) {
        refreshListener = listener
    }

    interface AddOn {

        /**
         * @param docPath : absolute path of file for which broadcast will be send to refresh
         * media database
         */
        fun refreshSystemMediaScanDataBase(path: String)
    }
}