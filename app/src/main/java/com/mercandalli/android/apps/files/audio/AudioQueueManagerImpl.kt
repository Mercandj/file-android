package com.mercandalli.android.apps.files.audio

import java.io.File

class AudioQueueManagerImpl(
        private val audioManager: AudioManager
) : AudioQueueManager {

    override fun next(path: String): String {
        var currentNextPath = nextPath(path)
        while (!audioManager.isSupportedPath(currentNextPath)) {
            currentNextPath = nextPath(path)
        }
        return currentNextPath
    }

    override fun previous(path: String): String {
        var currentPreviousPath = previousPath(path)
        while (!audioManager.isSupportedPath(currentPreviousPath)) {
            currentPreviousPath = previousPath(path)
        }
        return currentPreviousPath
    }

    companion object {

        fun nextPath(path: String): String {
            val ioFiles = computeIoFilesNextToPath(path)
            val extractIndex = extractIndex(path, ioFiles)
            if (extractIndex >= ioFiles.size - 1) {
                return ioFiles[0].absolutePath
            }
            return ioFiles[extractIndex + 1].absolutePath
        }

        fun previousPath(path: String): String {
            val ioFiles = computeIoFilesNextToPath(path)
            val extractIndex = extractIndex(path, ioFiles)
            if (extractIndex <= 0) {
                return ioFiles[ioFiles.size - 1].absolutePath
            }
            return ioFiles[extractIndex - 1].absolutePath
        }

        private fun computeIoFilesNextToPath(path: String): Array<out File> {
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