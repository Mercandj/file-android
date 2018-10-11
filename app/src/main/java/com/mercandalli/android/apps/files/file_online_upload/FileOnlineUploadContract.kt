package com.mercandalli.android.apps.files.file_online_upload

interface FileOnlineUploadContract {

    interface UserAction {

        fun onUploadClicked()

        fun onFileSelected(path: String, mime: String?)
    }

    interface Screen {

        fun setPath(text: String)

        fun quit()
    }

}