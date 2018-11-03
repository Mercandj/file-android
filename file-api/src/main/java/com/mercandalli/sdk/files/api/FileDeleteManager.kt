package com.mercandalli.sdk.files.api

interface FileDeleteManager {

    fun delete(path: String)

    fun registerFileDeleteCompletionListener(listener: FileDeleteCompletionListener)

    fun unregisterFileDeleteCompletionListener(listener: FileDeleteCompletionListener)

    interface FileDeleteCompletionListener {

        fun onFileDeletedCompleted(
            path: String,
            succeeded: Boolean
        )
    }
}
