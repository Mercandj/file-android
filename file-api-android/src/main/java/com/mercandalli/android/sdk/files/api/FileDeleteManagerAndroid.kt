package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.FileDeleteManager

class FileDeleteManagerAndroid(
        private val mediaScanner: MediaScanner
) : FileDeleteManager {

    override fun delete(path: String) {
        val ioFile = java.io.File(path)
        val parentPath = ioFile.parentFile.absolutePath
        if (ioFile.isDirectory) {
            deleteDirectory(ioFile)
        } else {
            ioFile.delete()
        }
        mediaScanner.refresh(path)
        mediaScanner.refresh(parentPath)
    }

    companion object {

        /**
         * Deletes a directory recursively.
         *
         * @param ioDirectory directory to delete
         */
        @JvmStatic
        fun deleteDirectory(ioDirectory: java.io.File): Boolean {
            if (ioDirectory.isDirectory) {
                val children = ioDirectory.list() ?: return ioDirectory.delete()
                for (str in children) {
                    val success = deleteDirectory(java.io.File(ioDirectory, str))
                    if (!success) {
                        return false
                    }
                }
            }
            // The directory is now empty so delete it
            return ioDirectory.delete()
        }
    }
}