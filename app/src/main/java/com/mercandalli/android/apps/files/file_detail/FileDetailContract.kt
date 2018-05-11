package com.mercandalli.android.apps.files.file_detail

import com.mercandalli.sdk.files.api.File

interface FileDetailContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onFileChanged(file: File?)

        fun onPlayPauseClicked()
    }

    interface Screen {

        fun setTitle(title: String)

        fun setPath(path: String)

        fun setLength(length: String)

        fun showPlayPauseButton()

        fun hidePlayPauseButton()

        fun setPlayPauseButtonText(stringRes: Int)
    }
}