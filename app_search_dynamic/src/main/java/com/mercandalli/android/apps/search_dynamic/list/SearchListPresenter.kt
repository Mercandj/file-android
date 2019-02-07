@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.search_dynamic.list

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileOpenManager

class SearchListPresenter(
    private val screen: SearchListContract.Screen,
    private val fileOpenManager: FileOpenManager
) : SearchListContract.UserAction {

    override fun onFileClicked(file: File) {
        fileOpenManager.open(file.path)
    }
}
