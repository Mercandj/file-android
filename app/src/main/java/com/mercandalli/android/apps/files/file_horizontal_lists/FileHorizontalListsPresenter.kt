package com.mercandalli.android.apps.files.file_horizontal_lists

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileOpenManager

class FileHorizontalListsPresenter(
        private val screen: FileHorizontalListsContract.Screen,
        private val fileOpenManager: FileOpenManager
) : FileHorizontalListsContract.UserAction {

    private var sizeLists = 1
    private var selectedFile: File? = null

    init {
        screen.hideFab()
    }

    override fun onFileClicked(index: Int, file: File) {
        if (file.path == selectedFile?.path) {
            sizeLists = 1
            screen.setListsSize(1)
            setSelectedFile(null)
            return
        }
        if (!file.directory) {
            sizeLists = index + 1
            screen.setListsSize(sizeLists)
            setSelectedFile(file)
            return
        }
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
        if (selectedFile == null) {
            throw IllegalStateException("Fab should be hidden")
        }
        if (selectedFile!!.directory) {
            throw IllegalStateException("Fab not supported for directories")
        } else {
            fileOpenManager.open(selectedFile!!.path)
        }
    }

    private fun setSelectedFile(file: File?) {
        selectedFile = file
        screen.selectPath(file?.path)
        if (file == null || file.directory) {
            screen.hideFab()
        } else {
            screen.showFab()
        }
    }
}