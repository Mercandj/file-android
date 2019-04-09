package com.mercandalli.android.apps.files.file_provider_root

import com.mercandalli.android.apps.files.file_provider.FileProvider

interface FileProviderRootManager {

    fun getFileRootPath(
        fileProvider: FileProvider
    ): String
}
