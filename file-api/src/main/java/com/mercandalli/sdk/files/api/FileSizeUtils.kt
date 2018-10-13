package com.mercandalli.sdk.files.api

object FileSizeUtils {

    fun computeSizeFromJavaFileSync(path: String): FileSizeResult {
        val file = java.io.File(path)
        if (!file.exists()) {
            return FileSizeResult.createErrorNotExists(path)
        }
        if (!file.isDirectory) {
            val length = file.length()
            return FileSizeResult.createLoaded(path, length)
        }
        val folderLength = computeSizeFromJavaFolderSync(file)
        return FileSizeResult.createLoaded(path, folderLength)
    }

    private fun computeSizeFromJavaFolderSync(directory: java.io.File): Long {
        var length: Long = 0
        for (file in directory.listFiles()) {
            length += if (file.isFile) {
                file.length()
            } else {
                computeSizeFromJavaFolderSync(file)
            }
        }
        return length
    }
}