@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_column_horizontal_lists

import com.mercandalli.android.apps.files.R
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileOpenManager

class FileColumnHorizontalListsPresenter(
    private val screen: FileColumnHorizontalListsContract.Screen,
    private val fileChildrenManager: FileChildrenManager,
    private val fileOpenManager: FileOpenManager,
    private val fileCopyCutManager: FileCopyCutManager,
    private val rootPath: String
) : FileColumnHorizontalListsContract.UserAction {

    private var sizeLists = 1
    private var selectedFile: File? = null
    private val fileToPasteChangedListener = createFileToPasteChangedListener()
    private val fileChildrenResultListener = createFileChildrenResultListener()

    init {
        screen.hideFab()
    }

    override fun onAttached() {
        fileCopyCutManager.registerFileToPasteChangedListener(fileToPasteChangedListener)
        syncFab()
        fileChildrenManager.registerFileChildrenResultListener(fileChildrenResultListener)
    }

    override fun onDetached() {
        fileCopyCutManager.unregisterFileToPasteChangedListener(fileToPasteChangedListener)
        fileChildrenManager.unregisterFileChildrenResultListener(fileChildrenResultListener)
    }

    override fun onFileClicked(index: Int, file: File) {
        if (file.path == selectedFile?.path) {
            sizeLists = 1
            screen.setListsSize(1)
            setSelectedFile(null)
            screen.hideFileDetailView()
            return
        }
        if (!file.directory) {
            sizeLists = index + 1
            screen.setListsSize(sizeLists)
            setSelectedFile(file)
            screen.showFileDetailView(file)
            screen.scrollEnd()
            return
        }
        screen.hideFileDetailView()
        if (index > sizeLists - 1) {
            throw IllegalStateException("index:$index sizeLists-1:" + (sizeLists - 1))
        }
        if (index == sizeLists - 1) {
            screen.createList(file.path, index + 1)
            screen.scrollEnd()
            setSelectedFile(file)
            sizeLists++
            return
        }
        screen.setPath(file.path, index + 1)
        sizeLists = index + 2
        screen.setListsSize(sizeLists)
        setSelectedFile(file)
    }

    override fun onFileLongClicked(index: Int, file: File) {
        // Nothing here for now
    }

    override fun onFabClicked() {
        val fileToPastePath = fileCopyCutManager.getFileToPastePath()
        if (fileToPastePath != null) {
            val pathOutput = when {
                selectedFile == null -> rootPath
                selectedFile!!.directory -> selectedFile!!.path
                else -> java.io.File(selectedFile!!.path).parentFile.absolutePath
            }
            fileCopyCutManager.paste(pathOutput)
            return
        }
        if (selectedFile == null) {
            throw IllegalStateException("Fab should be hidden")
        }
        if (selectedFile!!.directory) {
            throw IllegalStateException("Fab not supported for directories")
        } else {
            fileOpenManager.open(selectedFile!!.path)
        }
    }

    private fun syncFab() {
        val fileToPastePath = fileCopyCutManager.getFileToPastePath()
        if (fileToPastePath != null) {
            screen.setFabIcon(R.drawable.ic_content_paste_black_24dp)
            screen.showFab()
            return
        }
        if (selectedFile == null || selectedFile!!.directory) {
            screen.hideFab()
        } else {
            screen.hideFab()
        }
    }

    private fun setSelectedFile(file: File?) {
        selectedFile = file
        screen.selectPath(file?.path)
        if (file == null) {
            screen.hideFileDetailView()
        }
        syncFab()
    }

    private fun createFileToPasteChangedListener(): FileCopyCutManager.FileToPasteChangedListener {
        return object : FileCopyCutManager.FileToPasteChangedListener {
            override fun onFileToPasteChanged() {
                syncFab()
            }
        }
    }

    private fun createFileChildrenResultListener() = object : FileChildrenManager.FileChildrenResultListener {
        override fun onFileChildrenResultChanged(path: String) {
            if (selectedFile == null) {
                return
            }
            val selectedPath = selectedFile!!.path
            if (!java.io.File(selectedPath).exists()) {
                setSelectedFile(null)
            }
        }
    }
}
