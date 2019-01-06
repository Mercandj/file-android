@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_details

import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.FileSizeManager
import com.mercandalli.sdk.files.api.FileSizeUtils

class FileDetailsPresenter(
    private val screen: FileDetailsContract.Screen,
    private var fileManager: FileManager,
    private var fileChildrenManager: FileChildrenManager,
    private var fileSizeManager: FileSizeManager
) : FileDetailsContract.UserAction {

    private var path: String? = null
    private val fileResultListener = createFileResultListener()
    private val fileSizeResultListener = createFileSizeResultListener()

    override fun onCreate(path: String) {
        fileManager.registerFileResultListener(fileResultListener)
        fileSizeManager.registerFileSizeResultListener(fileSizeResultListener)

        this.path = path
        fileManager.loadFile(path)
        fileSizeManager.loadSize(path)
        syncFile()
    }

    override fun onDestroy() {
        fileManager.unregisterFileResultListener(fileResultListener)
        fileSizeManager.unregisterFileSizeResultListener(fileSizeResultListener)
    }

    override fun onSetFileManagers(
        fileManager: FileManager,
        fileChildrenManager: FileChildrenManager,
        fileSizeManager: FileSizeManager
    ) {
        this.fileManager = fileManager
        this.fileChildrenManager = fileChildrenManager
        this.fileSizeManager = fileSizeManager
    }

    private fun syncFile() {
        val path = if (path == null) {
            return
        } else {
            path!!
        }
        screen.setPathText(path)
        val fileResult = fileManager.getFile(path)
        fileResult.file?.let {
            screen.setNameText(it.name)
        }
        val fileSizeResult = fileSizeManager.getSize(path)
        val sizeLong = fileSizeResult.size
        val sizeString = FileSizeUtils.humanReadableByteCount(sizeLong)
        screen.setSizeText(sizeString)
    }

    private fun createFileResultListener() = object : FileManager.FileResultListener {
        override fun onFileResultChanged(path: String) {
            if (path != this@FileDetailsPresenter.path) {
                return
            }
            syncFile()
        }
    }

    private fun createFileSizeResultListener() = object : FileSizeManager.FileSizeResultListener {
        override fun onFileSizeResultChanged(path: String) {
            if (path != this@FileDetailsPresenter.path) {
                return
            }
            syncFile()
        }
    }
}
