package com.mercandalli.sdk.files.api

/**
 * Sub files container.
 */
data class FileResult(
    val path: String,
    val status: Status,
    val file: File?
) {

    enum class Status {
        ERROR_NETWORK,
        UNLOADED,
        LOADING,
        LOADED_SUCCEEDED
    }

    companion object {

        @JvmStatic
        fun createUnloaded(path: String) = FileResult(
            path,
            Status.UNLOADED,
            null
        )

        @JvmStatic
        fun createLoading(path: String) = FileResult(
            path,
            Status.LOADING,
            null
        )

        @JvmStatic
        fun createLoaded(path: String, file: File) = FileResult(
            path,
            Status.LOADED_SUCCEEDED,
            file
        )

        @JvmStatic
        fun createErrorNetwork(path: String) = FileResult(
            path,
            Status.ERROR_NETWORK,
            null
        )
    }
}
