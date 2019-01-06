@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_details

import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileManager

interface FileDetailsContract {

    interface UserAction {

        fun onCreate(path: String)

        fun onDestroy()

        fun onSetFileManagers(
            fileManager: FileManager,
            fileChildrenManager: FileChildrenManager
        )
    }

    interface Screen {

        fun setPathText(text: String)

        fun setNameText(text: String)
    }
}
