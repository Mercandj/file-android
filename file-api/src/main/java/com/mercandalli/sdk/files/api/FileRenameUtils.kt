package com.mercandalli.sdk.files.api

object FileRenameUtils {

    fun renameSync(path: String, fileName: String): Boolean {
        val javaFile = java.io.File(path)
        return renameSync(javaFile, fileName)
    }

    fun renameSync(javaFile: java.io.File, fileName: String): Boolean {
        val parentPath = javaFile.parentFile.absolutePath
        val ioFileOutput = java.io.File(parentPath, fileName)
        return javaFile.renameTo(ioFileOutput)
    }
}