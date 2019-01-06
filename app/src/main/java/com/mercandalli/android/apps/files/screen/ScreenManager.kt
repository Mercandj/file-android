package com.mercandalli.android.apps.files.screen

import com.mercandalli.android.apps.files.file.FileProvider

interface ScreenManager {

    fun showFileDetails(
        path: String,
        fileProvider: FileProvider
    )
}
