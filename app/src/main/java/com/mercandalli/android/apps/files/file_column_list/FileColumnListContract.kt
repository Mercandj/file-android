@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_column_list

import androidx.annotation.ColorRes
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenManager

interface FileColumnListContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onResume()

        fun onRefresh()

        fun onPathChanged(path: String)

        fun onPathSelected(path: String?)

        fun onSetFileManager(fileChildrenManager: FileChildrenManager)
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

        fun selectPath(path: String?)

        fun setEmptyTextColorRes(@ColorRes colorRes: Int)

        fun setErrorTextColorRes(@ColorRes colorRes: Int)
    }
}
