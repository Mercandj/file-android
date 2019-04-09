package com.mercandalli.android.sdk.files.api.internal

import com.mercandalli.sdk.files.api.FileRenameManager
import com.mercandalli.sdk.files.api.FileRenameUtils
import com.mercandalli.sdk.files.api.MediaScanner

internal class FileRenameManagerAndroid(
    private val mediaScanner: MediaScanner
) : FileRenameManager {

    override fun rename(path: String, fileName: String) {
        if (fileName.contains("/")) {
            return
        }
        val ioFileInput = java.io.File(path)
        val inputPath = ioFileInput.absolutePath
        val parentPath = ioFileInput.parentFile.absolutePath
        val ioFileOutput = java.io.File(parentPath, fileName)
        val outputPath = ioFileOutput.absolutePath
        FileRenameUtils.renameSync(inputPath, fileName)
        mediaScanner.refresh(path)
        mediaScanner.refresh(outputPath)
        mediaScanner.refresh(parentPath)
    }
}
