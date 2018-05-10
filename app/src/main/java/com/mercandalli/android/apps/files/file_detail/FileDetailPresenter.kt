package com.mercandalli.android.apps.files.file_detail

import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.sdk.files.api.File

class FileDetailPresenter(
        private val screen: FileDetailContract.Screen,
        private val audioManager: AudioManager
) : FileDetailContract.UserAction {

    private var file: File? = null

    override fun onFileChanged(file: File?) {
        this.file = file
        if (file == null) {
            screen.setTitle("")
            screen.setPath("")
            return
        }
        screen.setTitle(file.name)
        screen.setPath(file.path)
    }
}