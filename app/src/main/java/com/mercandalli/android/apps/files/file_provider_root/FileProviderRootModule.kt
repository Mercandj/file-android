package com.mercandalli.android.apps.files.file_provider_root

import com.mercandalli.android.apps.files.main.ApplicationGraph

class FileProviderRootModule {

    fun createFileProviderRootManager(): FileProviderRootManager {
        val fileRootManager = ApplicationGraph.getFileRootManager()
        return FileProviderRootManagerImpl(
            fileRootManager
        )
    }
}
