package com.mercandalli.sdk.files.api

/**
 * Sub files container.
 */
data class FileChildrenResult(
        val path: String,
        val status: Status,
        private val files: List<File>
) {

    fun getFiles(): List<File> = ArrayList<File>(files)

    enum class Status {
        ERROR_NOT_FOLDER,
        ERROR_NETWORK,
        UNLOADED,
        LOADING,
        LOADED_SUCCEEDED
    }

    companion object {

        @JvmStatic
        fun createUnloaded(path: String) = FileChildrenResult(
                path,
                Status.UNLOADED,
                ArrayList()
        )

        @JvmStatic
        fun createLoading(path: String) = FileChildrenResult(
                path,
                Status.LOADING,
                ArrayList()
        )

        @JvmStatic
        fun createLoaded(path: String, files: List<File>) = FileChildrenResult(
                path,
                Status.LOADED_SUCCEEDED,
                ArrayList(files)
        )

        @JvmStatic
        fun createErrorNotFolder(path: String) = FileChildrenResult(
                path,
                Status.ERROR_NOT_FOLDER,
                ArrayList()
        )

        @JvmStatic
        fun createErrorNetwork(path: String) = FileChildrenResult(
                path,
                Status.ERROR_NETWORK,
                ArrayList()
        )
    }
}