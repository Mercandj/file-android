package com.mercandalli.android.apps.files.file_horizontal_lists

import com.mercandalli.sdk.files.api.File

class FileHorizontalListsPresenter(
        private val screen: FileHorizontalListsContract.Screen
) : FileHorizontalListsContract.UserAction {

    private var sizeLists = 1

    override fun onFileClicked(index: Int, file: File) {
        if (!file.directory) {
            return
        }
        if (index > sizeLists - 1) {
            throw IllegalStateException("index:$index sizeLists-1:" + (sizeLists - 1))
        }
        if (index == sizeLists - 1) {
            screen.createList(file.path, index + 1)
            screen.scrollEnd()
            screen.selectPath(file.path)
            sizeLists++
            return
        }
        screen.setPath(file.path, index + 1)
        sizeLists = index + 2
        screen.setListsSize(index + 2)
        screen.selectPath(file.path)
    }

}