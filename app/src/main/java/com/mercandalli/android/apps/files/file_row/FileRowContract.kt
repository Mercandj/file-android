package com.mercandalli.android.apps.files.file_row

import com.mercandalli.sdk.files.api.File

interface FileRowContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()
    }

    interface Screen {

        fun showFile(file: File)
    }

}