@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_details

interface FileDetailsContract {

    interface UserAction {

        fun onCreate(path: String)
    }

    interface Screen {

        fun setPathText(text: String)
    }
}
