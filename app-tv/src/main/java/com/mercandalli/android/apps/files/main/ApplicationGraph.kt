package com.mercandalli.android.apps.files.main

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.apps.files.screen.ScreenModule
import com.mercandalli.android.apps.files.toast.ToastModule
import com.mercandalli.android.sdk.files.api.FileModule
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn

class ApplicationGraph(
    private val context: Context
) {

    private val fileModule by lazy { FileModule(context, createPermissionRequestAddOn()) }
    private val screenModule by lazy { ScreenModule(context) }

    private val fileChildrenManagerInternal by lazy { fileModule.createFileChildrenManager() }
    private val fileOpenManagerInternal by lazy { fileModule.createFileOpenManager() }
    private val fileSearchManagerInternal by lazy { fileModule.createFileSearchManager() }
    private val fileSortManagerInternal by lazy { fileModule.createFileSortManager() }
    private val screenManagerInternal by lazy { screenModule.createScreenManager() }
    private val toastManagerInternal by lazy { ToastModule(context).createToastManager() }

    private fun createPermissionRequestAddOn() = object : PermissionRequestAddOn {
        override fun requestStoragePermission() {
            screenManagerInternal.startPermission()
        }
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var graph: ApplicationGraph? = null

        fun init(context: Context) {
            if (graph == null) {
                val applicationContext = context.applicationContext
                graph = ApplicationGraph(
                    applicationContext
                )
            }
        }

        fun getFileChildrenManager() = graph!!.fileChildrenManagerInternal
        fun getFileOpenManager() = graph!!.fileOpenManagerInternal
        fun getFileSearchManager() = graph!!.fileSearchManagerInternal
        fun getFileSortManager() = graph!!.fileSortManagerInternal
        fun getScreenManager() = graph!!.screenManagerInternal
        fun getToastManager() = graph!!.toastManagerInternal
    }
}
