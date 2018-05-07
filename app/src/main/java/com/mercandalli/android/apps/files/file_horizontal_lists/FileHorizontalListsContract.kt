package com.mercandalli.android.apps.files.file_horizontal_lists

import com.mercandalli.sdk.files.api.File

interface FileHorizontalListsContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onFileClicked(index: Int, file: File)

        fun onFileLongClicked(index: Int, file: File)

        fun onFabClicked()
    }

    interface Screen {

        fun createList(path: String, index: Int)

        fun setPath(path: String, index: Int)

        fun setListsSize(size: Int)

        fun scrollEnd()

        fun selectPath(path: String?)

        fun showFab()

        fun hideFab()

        fun setFabIcon(drawableRes: Int)
    }
}