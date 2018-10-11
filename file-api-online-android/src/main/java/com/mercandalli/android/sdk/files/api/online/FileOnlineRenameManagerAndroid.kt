package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.FileRenameManager
import com.mercandalli.sdk.files.api.MediaScanner

internal class FileOnlineRenameManagerAndroid(
        private val fileOnlineApi: FileOnlineApi,
        private val mediaScanner: MediaScanner
) : FileRenameManager {

    override fun rename(path: String, fileName: String) {
        val ioFile = java.io.File(path)
        val parentPath = ioFile.parentFile.absolutePath
        val ioFileOutput = java.io.File(parentPath, fileName)
        val outputPath = ioFileOutput.absolutePath
        fileOnlineApi.rename(path, fileName)
        mediaScanner.refresh(path)
        mediaScanner.refresh(outputPath)
        mediaScanner.refresh(parentPath)
    }
}