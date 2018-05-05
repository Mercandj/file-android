package com.mercandalli.android.apps.files.file_horizontal_lists

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileOpenManager

class FileHorizontalListsPresenter(
        private val screen: FileHorizontalListsContract.Screen,
        private val fileOpenManager: FileOpenManager
) : FileHorizontalListsContract.UserAction {

    private var sizeLists = 1
    private var selectedPath: String? = null

    override fun onFileClicked(index: Int, file: File) {
        val path = file.path
        if (path == selectedPath) {
            sizeLists = 1
            screen.setListsSize(1)
            selectedPath = null
            screen.selectPath(selectedPath)
            return
        }
        if (!file.directory) {
            fileOpenManager.open(path)
            return
        }
        if (index > sizeLists - 1) {
            throw IllegalStateException("index:$index sizeLists-1:" + (sizeLists - 1))
        }
        if (index == sizeLists - 1) {
            screen.createList(path, index + 1)
            screen.scrollEnd()
            selectedPath = path
            screen.selectPath(path)
            sizeLists++
            return
        }
        screen.setPath(path, index + 1)
        sizeLists = index + 2
        screen.setListsSize(index + 2)
        selectedPath = path
        screen.selectPath(path)
    }

}