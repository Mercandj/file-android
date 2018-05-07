package com.mercandalli.android.apps.files.main

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.mercandalli.android.apps.files.permission.PermissionActivity
import com.mercandalli.android.sdk.files.api.FileModule
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.FileOpenManager

class ApplicationGraph(
        private val context: Context
) {

    private lateinit var fileManager: FileManager
    private lateinit var fileOpenManager: FileOpenManager
    private lateinit var fileDeleteManager: FileDeleteManager
    private lateinit var fileCopyCutManager: FileCopyCutManager
    private lateinit var fileModule: FileModule

    private fun getFileManagerInternal(): FileManager {
        if (!::fileManager.isInitialized) {
            if (!::fileModule.isInitialized) {
                fileModule = createFileModule()
            }
            fileManager = fileModule.provideFileManager()
        }
        return fileManager
    }

    private fun getFileOpenManagerInternal(): FileOpenManager {
        if (!::fileOpenManager.isInitialized) {
            if (!::fileModule.isInitialized) {
                fileModule = createFileModule()
            }
            fileOpenManager = fileModule.provideFileOpenManager()
        }
        return fileOpenManager
    }

    private fun getFileDeleteManagerInternal(): FileDeleteManager {
        if (!::fileDeleteManager.isInitialized) {
            if (!::fileModule.isInitialized) {
                fileModule = createFileModule()
            }
            fileDeleteManager = fileModule.provideFileDeleteManager()
        }
        return fileDeleteManager
    }

    private fun getFileCopyCutManagerInternal(): FileCopyCutManager {
        if (!::fileCopyCutManager.isInitialized) {
            if (!::fileModule.isInitialized) {
                fileModule = createFileModule()
            }
            fileCopyCutManager = fileModule.provideFileCopyCutManager()
        }
        return fileCopyCutManager
    }

    private fun createFileModule(): FileModule {
        val permissionRequestAddOn: PermissionRequestAddOn = object : PermissionRequestAddOn {
            override fun requestStoragePermission() {
                PermissionActivity.start(context)
            }
        }
        return FileModule(context, permissionRequestAddOn)
    }

    companion object {

        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        private var graph: ApplicationGraph? = null

        @JvmStatic
        fun getFileManager(): FileManager {
            return graph!!.getFileManagerInternal()
        }

        @JvmStatic
        fun getFileOpenManager(): FileOpenManager {
            return graph!!.getFileOpenManagerInternal()
        }

        @JvmStatic
        fun getFileDeleteManager(): FileDeleteManager {
            return graph!!.getFileDeleteManagerInternal()
        }

        @JvmStatic
        fun getFileCopyCutManager(): FileCopyCutManager {
            return graph!!.getFileCopyCutManagerInternal()
        }

        @JvmStatic
        fun init(context: Context) {
            if (graph == null) {
                graph = ApplicationGraph(context.applicationContext)
            }
        }
    }
}