package com.mercandalli.sdk.files.api

/**
 * Sub files container.
 */
data class FileSearchResult(
    val query: String,
    val status: Status,
    val files: List<File>
) {

    enum class Status {
        ERROR_NETWORK,
        UNLOADED,
        LOADING,
        LOADED_SUCCEEDED
    }

    companion object {

        @JvmStatic
        fun createUnloaded(path: String) = FileSearchResult(
            path,
            Status.UNLOADED,
            ArrayList()
        )

        @JvmStatic
        fun createLoading(path: String) = FileSearchResult(
            path,
            Status.LOADING,
            ArrayList()
        )

        @JvmStatic
        fun createLoaded(path: String, files: List<File>) = FileSearchResult(
            path,
            Status.LOADED_SUCCEEDED,
            files
        )

        @JvmStatic
        fun createErrorNetwork(path: String) = FileSearchResult(
            path,
            Status.ERROR_NETWORK,
            ArrayList()
        )
    }
}
