package com.mercandalli.android.apps.files.audio

import com.mercandalli.sdk.files.api.FileSortManager
import java.io.File

class AudioQueueManagerImpl(
        private val audioManager: AudioManager,
        private val fileSortManager: FileSortManager
) : AudioQueueManager {

    override fun next(path: String): String {
        val ioFiles = computeIoFilesInSameDirectoryIoFiles(path)
        val ioFilesSorted = fileSortManager.sortIoFiles(ioFiles)
        var currentNextPath = nextPath(path, ioFilesSorted)
        while (!audioManager.isSupportedPath(currentNextPath)) {
            currentNextPath = nextPath(path, ioFilesSorted)
        }
        return currentNextPath
    }

    override fun previous(path: String): String {
        val ioFiles = computeIoFilesInSameDirectoryIoFiles(path)
        val ioFilesSorted = fileSortManager.sortIoFiles(ioFiles)
        var currentPreviousPath = previousPath(path, ioFilesSorted)
        while (!audioManager.isSupportedPath(currentPreviousPath)) {
            currentPreviousPath = previousPath(path, ioFilesSorted)
        }
        return currentPreviousPath
    }

    companion object {

        fun nextPath(path: String, ioFiles: Array<out File>): String {
            val extractIndex = extractIndex(path, ioFiles)
            if (extractIndex >= ioFiles.size - 1) {
                return ioFiles[0].absolutePath
            }
            return ioFiles[extractIndex + 1].absolutePath
        }

        fun previousPath(path: String, ioFiles: Array<out File>): String {
            val extractIndex = extractIndex(path, ioFiles)
            if (extractIndex <= 0) {
                return ioFiles[ioFiles.size - 1].absolutePath
            }
            return ioFiles[extractIndex - 1].absolutePath
        }

        private fun computeIoFilesInSameDirectoryIoFiles(path: String): Array<out File> {
            val ioFile = java.io.File(path)
            return ioFile.parentFile.listFiles()
        }

        private fun extractIndex(path: String, ioFiles: Array<out File>): Int {
            for (i in 0 until ioFiles.size) {
                if (ioFiles[i].absolutePath == path) {
                    return i
                }
            }
            throw IllegalStateException("Path $path not found")
        }
    }
}