package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.FileDeleteManager

class FileDeleteManagerAndroid(
        private val addOn: AddOn
) : FileDeleteManager {

    override fun delete(path: String) {
        val ioFile = java.io.File(path)
        ioFile.delete()
        addOn.refreshSystemMediaScanDataBase(path)
    }

    interface AddOn {

        /**
         * @param docPath : absolute path of file for which broadcast will be send to refresh
         * media database
         */
        fun refreshSystemMediaScanDataBase(path: String)
    }
}