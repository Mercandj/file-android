package com.mercandalli.sdk.files.api

object FileRenameUtils {

    fun renameSync(path: String, fileName: String): Boolean {
        val ioFile = java.io.File(path)
        val parentPath = ioFile.parentFile.absolutePath
        val ioFileOutput = java.io.File(parentPath, fileName)
        return ioFile.renameTo(ioFileOutput)
    }
}