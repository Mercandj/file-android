package com.mercandalli.android.apps.files.main

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.mercandalli.android.apps.files.permission.PermissionActivity
import com.mercandalli.android.sdk.files.api.FileModule
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.FileOpenManager

class ApplicationGraph(
        private val context: Context
) {

    private var fileManager: FileManager? = null
    private var fileOpenManager: FileOpenManager? = null
    private var fileModule: FileModule? = null

    fun getFileManagerInternal(): FileManager {
        if (fileManager == null) {
            if (fileModule == null) {
                fileModule = createFileModule()
            }
            fileManager = fileModule!!.provideFileManager()
        }
        return fileManager!!
    }

    fun getFileOpenManagerInternal(): FileOpenManager {
        if (fileOpenManager == null) {
            if (fileModule == null) {
                fileModule = createFileModule()
            }
            fileOpenManager = fileModule!!.provideFileOpenManager()
        }
        return fileOpenManager!!
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
        fun init(context: Context) {
            if (graph == null) {
                graph = ApplicationGraph(context.applicationContext)
            }
        }
    }
}