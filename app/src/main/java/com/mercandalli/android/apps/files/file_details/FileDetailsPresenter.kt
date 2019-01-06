@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_details

import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileManager

class FileDetailsPresenter(
    private val screen: FileDetailsContract.Screen,
    private var fileManager: FileManager,
    private var fileChildrenManager: FileChildrenManager
) : FileDetailsContract.UserAction {

    private var path: String? = null
    private val fileResultListener = createFileResultListener()

    override fun onCreate(path: String) {
        fileManager.registerFileResultListener(fileResultListener)

        this.path = path
        fileManager.loadFile(path)
        syncFile()
    }

    override fun onDestroy() {
        fileManager.unregisterFileResultListener(fileResultListener)
    }

    override fun onSetFileManagers(
        fileManager: FileManager,
        fileChildrenManager: FileChildrenManager
    ) {
        this.fileManager = fileManager
        this.fileChildrenManager = fileChildrenManager
    }

    private fun syncFile() {
        val fileResult = path!!.let {
            screen.setPathText(it)
            fileManager.getFile(it)
        }
        fileResult.file?.let {
            screen.setNameText(it.name)
        }
    }

    private fun createFileResultListener() = object : FileManager.FileResultListener {
        override fun onFileResultChanged(path: String) {
            if (path != this@FileDetailsPresenter.path) {
                return
            }
            syncFile()
        }
    }
}
