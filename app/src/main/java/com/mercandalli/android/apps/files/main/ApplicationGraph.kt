package com.mercandalli.android.apps.files.main

import com.mercandalli.android.sdk.files.api.FileManager
import com.mercandalli.android.sdk.files.api.FileModule

class ApplicationGraph {

    companion object {

        private lateinit var fileManager: FileManager

        @JvmStatic
        fun getFileManager(): FileManager {
            if (!Companion::fileManager.isInitialized) {
                fileManager = FileModule().provideFileManager()
            }
            return fileManager
        }

    }

}