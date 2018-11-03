@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_online

interface FileOnlineContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onFileOpenClicked(path: String)
    }

    interface Screen
}
