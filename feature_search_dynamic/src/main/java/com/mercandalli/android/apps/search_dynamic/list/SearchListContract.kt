@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.search_dynamic.list

import com.mercandalli.sdk.files.api.File

interface SearchListContract {

    interface UserAction {

        fun onFileClicked(file: File)
    }

    interface Screen
}
