package com.mercandalli.android.apps.files.file_column_row

import androidx.annotation.ColorRes
import com.mercandalli.sdk.files.api.File

interface FileColumnRowContract {

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
    }

    interface Screen {

        fun setTitle(title: String)

        fun setRightIconVisibility(visible: Boolean)

        fun setRightIconDrawableRes(drawableRightIconDirectoryDrawableRes: Int)

        fun setIcon(directory: Boolean)

        fun setRowSelected(selected: Boolean)

        fun notifyRowClicked(file: File)

        fun notifyRowLongClicked(file: File)

        fun showOverflowPopupMenu()

        fun showDeleteConfirmation(fileName: String)

        fun showRenamePrompt(fileName: String)

        fun setTextColorRes(@ColorRes colorRes: Int)

        fun setBackgroundColorRes(@ColorRes colorRes: Int)
    }
}