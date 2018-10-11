package com.mercandalli.android.apps.files.file_online_upload

import com.mercandalli.android.sdk.files.api.online.FileOnlineUploadManager
import com.mercandalli.sdk.files.api.File
import java.util.*

class FileOnlineUploadPresenter(
        private val screen: FileOnlineUploadContract.Screen,
        private val fileOnlineUploadManager: FileOnlineUploadManager
) : FileOnlineUploadContract.UserAction {

    private var path: String? = null

    override fun onUploadClicked() {
        if (path == null) {
            return
        }
        val javaFile = java.io.File(path)
        val file = File.create(
                UUID.randomUUID().toString(),
                "/${javaFile.name}",
                "/",
                false,
                javaFile.name,
                javaFile.length(),
                javaFile.lastModified()
        )
        fileOnlineUploadManager.upload(
                file,
                javaFile
        )
        screen.quit()
    }

    override fun onFileSelected(path: String, mime: String?) {
        this.path = path
        screen.setPath(path)
    }
}