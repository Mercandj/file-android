package com.mercandalli.sdk.files.api

object FileSizeUtils {

    fun humanReadableByteCount(bytes: Long, si: Boolean = true): String {
        val unit = if (si) {
            1_000
        } else {
            1_024
        }
        if (bytes < unit) {
            return bytes.toString() + " B"
        }
        val exp = (
                Math.log(bytes.toDouble())
                        /
                        Math.log(unit.toDouble())
                ).toInt()
        val units = if (si) {
            "kMGTPE"
        } else {
            "KMGTPE"
        }
        val pre = units[exp - 1] + if (si) {
            ""
        } else {
            "i"
        }
        return String.format(
                "%.1f %sB",
                bytes /
                        Math.pow(
                                unit.toDouble(),
                                exp.toDouble()
                        ),
                pre
        )
    }

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