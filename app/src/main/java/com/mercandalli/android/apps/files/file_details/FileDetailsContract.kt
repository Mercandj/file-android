@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_details

import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.FileSizeManager

interface FileDetailsContract {

    interface UserAction {

        fun onCreate(path: String)

        fun onDestroy()

        fun onSetFileManagers(
            fileManager: FileManager,
            fileChildrenManager: FileChildrenManager,
            fileSizeManager: FileSizeManager
        )
    }

    interface Screen {

        fun setPathText(text: String)

        fun setNameText(text: String)

        fun setSizeText(text: String)
    }
}
