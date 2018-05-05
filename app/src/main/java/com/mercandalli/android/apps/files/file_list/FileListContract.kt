package com.mercandalli.android.apps.files.file_list

import com.mercandalli.sdk.files.api.File

interface FileListContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onResume()

        fun onRefresh()

        fun onPathChanged(path: String)
    }

    interface Screen {

        fun showEmptyView()

        fun hideEmptyView()

        fun showErrorMessage()

        fun hideErrorMessage()

        fun showFiles(files: List<File>)

        fun hideFiles()

        fun showLoader()

        fun hideLoader()
    }
}