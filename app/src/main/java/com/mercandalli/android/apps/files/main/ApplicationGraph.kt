package com.mercandalli.android.apps.files.main

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.mercandalli.android.apps.files.permission.PermissionActivity
import com.mercandalli.android.sdk.files.api.FileModule
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn
import com.mercandalli.sdk.files.api.FileManager

class ApplicationGraph(
        private val context: Context
) {

    private var fileManager: FileManager? = null

    fun getFileManagerInternal(): FileManager {
        if (fileManager == null) {
            val permissionRequestAddOn: PermissionRequestAddOn = object : PermissionRequestAddOn {
                override fun requestStoragePermission() {
                    PermissionActivity.start(context)
                }
            }
            fileManager = FileModule(context, permissionRequestAddOn).provideFileManager()
        }
        return fileManager!!
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
        fun init(context: Context) {
            if (graph == null) {
                graph = ApplicationGraph(context.applicationContext)
            }
        }
    }
}