package com.mercandalli.android.apps.files.screen

import com.mercandalli.android.apps.files.file_provider.FileProvider

interface ScreenManager {

    fun startFileDetails(
        path: String,
        fileProvider: FileProvider
    )

    fun startPermission()

    fun startSystemSettingsStorage()

    fun startSearch()
}
