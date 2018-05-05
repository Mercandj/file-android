package com.mercandalli.sdk.files.api

import android.support.annotation.IntDef

/**
 * Sub files container.
 */
data class FileChildrenResult(
        val path: String,
        @Status val status: Int,
        private val files: List<File>
) {

    fun getFiles(): List<File> {
        return ArrayList<File>(files)
    }

    companion object {

        @IntDef(
                ERROR_NOT_FOLDER,
                UNLOADED,
                LOADING,
                LOADED_SUCCEEDED)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Status

        const val ERROR_NOT_FOLDER = -1
        const val UNLOADED = 0
        const val LOADING = 1
        const val LOADED_SUCCEEDED = 2

        @JvmStatic
        fun createUnloaded(path: String): FileChildrenResult {
            return FileChildrenResult(path, FileChildrenResult.UNLOADED, ArrayList())
        }

        @JvmStatic
        fun createLoading(path: String): FileChildrenResult {
            return FileChildrenResult(path, FileChildrenResult.LOADING, ArrayList())
        }

        @JvmStatic
        fun createLoaded(path: String, files: List<File>): FileChildrenResult {
            return FileChildrenResult(path, FileChildrenResult.LOADED_SUCCEEDED, ArrayList(files))
        }

        @JvmStatic
        fun createErrorNotFolder(path: String): FileChildrenResult {
            return FileChildrenResult(path, FileChildrenResult.ERROR_NOT_FOLDER, ArrayList())
        }
    }
}