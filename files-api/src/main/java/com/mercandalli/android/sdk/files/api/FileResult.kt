package com.mercandalli.android.sdk.files.api

import android.support.annotation.IntDef

data class FileResult(
        val path: String,
        @Status val status: Int,
        val file: File?
) {

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
        fun createUnloaded(path: String): FileResult {
            return FileResult(path, UNLOADED, null)
        }

        @JvmStatic
        fun createLoading(path: String): FileResult {
            return FileResult(path, LOADING, null)
        }

        @JvmStatic
        fun createLoaded(path: String, file: File): FileResult {
            return FileResult(path, LOADED, file)
        }

    }


}