package com.mercandalli.android.apps.files.file_row

import com.mercandalli.sdk.files.api.File

interface FileRowContract {

    interface UserAction {

        fun onFileChanged(file: File, selectedPath: String?)

        fun onRowClicked()

        fun onRowLongClicked()

        fun onCopyClicked()

        fun onCutClicked()

        fun onDeleteClicked()
    }

    interface Screen {

        fun setTitle(title: String)

        fun setArrowRightVisibility(visible: Boolean)

        fun setIcon(directory: Boolean)

        fun setRowSelected(selected: Boolean)

        fun notifyRowClicked(file: File)

        fun notifyRowLongClicked(file: File)

        fun showOverflowPopupMenu()
    }
}