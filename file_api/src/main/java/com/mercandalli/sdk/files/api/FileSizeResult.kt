package com.mercandalli.sdk.files.api

/**
 * File size container.
 */
data class FileSizeResult(
    val path: String,
    val status: Status,
    val size: Long
) {

    enum class Status {
        ERROR_NOT_EXIST,
        ERROR_NETWORK,
        UNLOADED,
        LOADING,
        LOADED_SUCCEEDED
    }

    companion object {

        @JvmStatic
        fun createUnloaded(path: String) = FileSizeResult(
            path,
            Status.UNLOADED,
            -1
        )

        @JvmStatic
        fun createLoading(path: String) = FileSizeResult(
            path,
            Status.LOADING,
            -1
        )

        @JvmStatic
        fun createLoaded(path: String, size: Long) = FileSizeResult(
            path,
            Status.LOADED_SUCCEEDED,
            size
        )

        @JvmStatic
        fun createErrorNotExists(path: String) = FileSizeResult(
            path,
            Status.ERROR_NOT_EXIST,
            -1
        )

        @JvmStatic
        fun createErrorNetwork(path: String) = FileSizeResult(
            path,
            Status.ERROR_NETWORK,
            -1
        )
    }
}
