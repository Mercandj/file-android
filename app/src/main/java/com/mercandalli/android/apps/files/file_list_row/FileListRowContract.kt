package com.mercandalli.android.apps.files.file_list_row

import com.mercandalli.sdk.files.api.File

interface FileListRowContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onFileChanged(file: File, selectedPath: String?)

        fun onRowClicked()

        fun onRowLongClicked()

        fun onCopyClicked()

        fun onCutClicked()

        fun onDeleteClicked()

        fun onDeleteConfirmedClicked()

        fun onRenameClicked()

        fun onRenameConfirmedClicked(fileName: String)

        fun onOverflowClicked()
    }

    interface Screen {

        fun setTitle(title: String)

        fun setSubtitle(subtitle: String)

        fun setSoundIconVisibility(visible: Boolean)

        fun setIcon(directory: Boolean)

        fun notifyRowClicked(file: File)

        fun notifyRowLongClicked(file: File)

        fun showOverflowPopupMenu()

        fun showDeleteConfirmation(fileName: String)

        fun showRenamePrompt(fileName: String)

        fun setTitleTextColorRes(textColorRes: Int)

        fun setSubtitleTextColorRes(textColorRes: Int)

        fun setCardBackgroundColorRes(cardBackgroundColorRes: Int)
    }
}