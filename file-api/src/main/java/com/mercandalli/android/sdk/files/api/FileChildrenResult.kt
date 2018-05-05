package com.mercandalli.android.sdk.files.api

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
                UNLOADED,
                LOADING,
                LOADED)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Status

        const val UNLOADED = 0
        const val LOADING = 1
        const val LOADED = 2

        @JvmStatic
        fun createUnloaded(path: String): FileChildrenResult {
            return FileChildrenResult(path, FileResult.UNLOADED, ArrayList())
        }

        @JvmStatic
        fun createLoading(path: String): FileChildrenResult {
            return FileChildrenResult(path, FileResult.LOADING, ArrayList())
        }

        @JvmStatic
        fun createLoaded(path: String, files: List<File>): FileChildrenResult {
            return FileChildrenResult(path, FileResult.LOADED, ArrayList(files))
        }
    }
}