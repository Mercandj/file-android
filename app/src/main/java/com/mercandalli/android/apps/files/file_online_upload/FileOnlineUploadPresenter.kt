package com.mercandalli.android.apps.files.file_online_upload

import com.mercandalli.android.sdk.files.api.online.FileOnlineUploadManager
import com.mercandalli.sdk.files.api.File

class FileOnlineUploadPresenter(
        private val screen: FileOnlineUploadContract.Screen,
        private val fileOnlineUploadManager: FileOnlineUploadManager
) : FileOnlineUploadContract.UserAction {

    override fun onUploadClicked() {
        fileOnlineUploadManager.upload(File.createFake("test"))
        screen.quit()
    }
}