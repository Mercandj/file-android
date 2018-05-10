package com.mercandalli.android.apps.files.file_detail

import com.mercandalli.sdk.files.api.File

interface FileDetailContract {

    interface UserAction {

        fun onFileChanged(file: File?)
    }

    interface Screen {

        fun setTitle(title: String)

        fun setPath(path: String)
    }
}