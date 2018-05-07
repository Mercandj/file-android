package com.mercandalli.sdk.files.api

interface FileCopyCutManager {

    fun copy(path: String)

    fun copy(pathInput: String, pathDirectoryOutput: String)

    fun cut(path: String)

    fun cut(pathInput: String, pathDirectoryOutput: String)

    fun getFileToPastePath(): String?

    fun paste(pathDirectoryOutput: String)

    fun cancelCopyCut()

    fun registerFileToPasteChangedListener(listener: FileToPasteChangedListener)

    fun unregisterFileToPasteChangedListener(listener: FileToPasteChangedListener)

    fun registerPasteListener(listener: PasteListener)

    fun unregisterPasteListener(listener: PasteListener)

    interface FileToPasteChangedListener {
        fun onFileToPasteChanged()
    }

    interface PasteListener {
        fun onPasteEnded(pathInput: String, pathOutput: String)
    }
}