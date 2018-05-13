package com.mercandalli.android.apps.files.file_detail

import com.mercandalli.sdk.files.api.File
import java.util.*

interface FileDetailContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onFileChanged(file: File?)

        fun onPlayPauseClicked()

        fun onNextClicked()

        fun onPreviousClicked()

        fun onDeleteClicked()

        fun onDeleteConfirmedClicked()
    }

    interface Screen {

        fun setTitle(title: String)

        fun setPath(path: String)

        fun setLength(length: String)

        fun setLastModified(lastModifiedDateString: String)

        fun showPlayPauseButton()

        fun hidePlayPauseButton()

        fun showNextButton()

        fun hideNextButton()

        fun showPreviousButton()

        fun hidePreviousButton()

        fun setPlayPauseButtonImage(drawableRes: Int)

        fun showDeleteConfirmation(fileName: String)
    }
}