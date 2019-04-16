package com.mercandalli.android.sdk.files.api.internal

import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FilePathManager
import com.mercandalli.sdk.files.api.MediaScanner
import java.io.File

internal class FileDeleteManagerAndroid(
    private val mediaScanner: MediaScanner,
    private val filePathManager: FilePathManager,
    private val addOn: AddOn
) : FileDeleteManager {

    private val listeners = ArrayList<FileDeleteManager.FileDeleteCompletionListener>()

    override fun delete(
        path: String
    ) {
        val deleteSucceeded = if (path.startsWith("content://")) {
            addOn.deleteFromContentResolver(
                path
            )
        } else {
            val ioFile = java.io.File(path)
            val deleteSucceeded = if (ioFile.isDirectory) {
                deleteDirectory(ioFile)
            } else {
                ioFile.delete()
            }
            deleteSucceeded
        }
        val parentPath = filePathManager.getParentPath(path)
        mediaScanner.refresh(path)
        if (parentPath != null) {
            mediaScanner.refresh(parentPath)
        }
        for (listener in listeners) {
            listener.onFileDeletedCompleted(
                path,
                deleteSucceeded
            )
        }
    }

    override fun registerFileDeleteCompletionListener(
        listener: FileDeleteManager.FileDeleteCompletionListener
    ) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterFileDeleteCompletionListener(
        listener: FileDeleteManager.FileDeleteCompletionListener
    ) {
        listeners.remove(listener)
    }

    interface AddOn {

        fun deleteFromContentResolver(
            path: String
        ): Boolean
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
                    val success = deleteDirectory(File(ioDirectory, str))
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
