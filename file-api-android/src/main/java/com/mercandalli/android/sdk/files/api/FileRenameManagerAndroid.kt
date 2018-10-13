package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.FileRenameManager
import com.mercandalli.sdk.files.api.FileRenameUtils
import com.mercandalli.sdk.files.api.MediaScanner

class FileRenameManagerAndroid(
        private val mediaScanner: MediaScanner
) : FileRenameManager {

    override fun rename(path: String, fileName: String) {
        val ioFile = java.io.File(path)
        val parentPath = ioFile.parentFile.absolutePath
        val ioFileOutput = java.io.File(parentPath, fileName)
        val outputPath = ioFileOutput.absolutePath
        FileRenameUtils.renameSync(parentPath, fileName)
        mediaScanner.refresh(path)
        mediaScanner.refresh(outputPath)
        mediaScanner.refresh(parentPath)
    }
}