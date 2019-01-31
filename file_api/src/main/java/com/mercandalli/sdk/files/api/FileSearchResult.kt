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
        fun createUnloaded(query: String) = FileSearchResult(
            query,
            Status.UNLOADED,
            ArrayList()
        )

        @JvmStatic
        fun createLoading(query: String) = FileSearchResult(
            query,
            Status.LOADING,
            ArrayList()
        )

        @JvmStatic
        fun createLoaded(query: String, files: List<File>) = FileSearchResult(
            query,
            Status.LOADED_SUCCEEDED,
            files
        )

        @JvmStatic
        fun createErrorNetwork(query: String) = FileSearchResult(
            query,
            Status.ERROR_NETWORK,
            ArrayList()
        )
    }
}
