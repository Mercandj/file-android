package com.mercandalli.android.apps.files.file_row

import com.mercandalli.sdk.files.api.File

class FileRowPresenter(
        private val screen: FileRowContract.Screen
) : FileRowContract.UserAction {

    private var file:File?=null

    override fun onFileChanged(file: File) {
        this.file = file
        screen.setTitle(file.name)
        val directory = file.directory
        screen.setArrowRightVisibility(directory)
        screen.setIcon(directory)
    }

    override fun onRowClicked() {
        screen.notifyRowClicked(file!!)
    }
}