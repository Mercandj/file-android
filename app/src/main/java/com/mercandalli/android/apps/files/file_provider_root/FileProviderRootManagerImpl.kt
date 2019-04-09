package com.mercandalli.android.apps.files.file_provider_root

import com.mercandalli.android.apps.files.file_provider.FileProvider
import com.mercandalli.sdk.files.api.FileRootManager

class FileProviderRootManagerImpl(
    private val fileRootManager: FileRootManager
) : FileProviderRootManager {

    override fun getFileRootPath(
        fileProvider: FileProvider
    ): String {
        @Suppress("UNUSED_EXPRESSION")
        return when (fileProvider) {
            FileProvider.Local -> fileRootManager.getFileRootPath()
            FileProvider.Online -> "/"
        }
    }
}
