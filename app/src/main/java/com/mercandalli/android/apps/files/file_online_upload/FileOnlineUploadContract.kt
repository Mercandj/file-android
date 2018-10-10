package com.mercandalli.android.apps.files.file_online_upload

interface FileOnlineUploadContract {

    interface UserAction {

        fun onUploadClicked()
    }

    interface Screen {

        fun quit()
    }

}