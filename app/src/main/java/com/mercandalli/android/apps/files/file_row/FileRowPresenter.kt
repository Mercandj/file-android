package com.mercandalli.android.apps.files.file_row

import com.mercandalli.sdk.files.api.File

class FileRowPresenter(
        private val screen: FileRowContract.Screen
) : FileRowContract.UserAction {

    override fun onFileChanged(file: File) {
        screen.setTitle(file.name)
    }
}