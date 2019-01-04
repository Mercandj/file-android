@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_details

class FileDetailsPresenter(
    private val screen: FileDetailsContract.Screen
) : FileDetailsContract.UserAction {

    override fun onCreate(path: String) {
        screen.setPathText(path)
    }
}
