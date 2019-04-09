package com.mercandalli.android.sdk.files.api.internal.file_children

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import com.mercandalli.android.sdk.files.api.PermissionManager
import com.mercandalli.android.sdk.files.api.RecursiveFileObserver
import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileRootManager
import com.mercandalli.sdk.files.api.MediaScanner
import java.io.File

internal class FileChildrenModule(
    private val context: Context,
    private val mediaScanner: MediaScanner,
    private val permissionManager: PermissionManager,
    private val fileRootManager: FileRootManager
) {

    fun createFileChildrenManager(): FileChildrenManager {
        val fileChildrenResultLoader = createFileChildrenResultLoader()
        val fileChildrenManager = FileChildrenManagerAndroid(
            fileChildrenResultLoader,
            permissionManager
        )
        val fileObserver = RecursiveFileObserver(
            fileRootManager.getFileRootPath()
        ) {
            if (it != null && !it.endsWith("/null")) {
                val path = File(it).parentFile.absolutePath
                fileChildrenManager.refresh(path)
            }
        }
        mediaScanner.addListener(object : MediaScanner.RefreshListener {
            override fun onContentChanged(path: String) {
                fileChildrenManager.refresh(path)
            }
        })
        fileObserver.startWatching()
        return fileChildrenManager
    }

    @SuppressLint("NewApi")
    private fun createFileChildrenResultLoader(): FileChildrenResultLoader {
        val fileChildrenResultLoaderFile = FileChildrenResultLoaderFile()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return fileChildrenResultLoaderFile
        }
        val fileChildrenResultLoaderContentResolver = FileChildrenResultLoaderContentResolver(
            context.contentResolver
        )
        return FileChildrenResultLoaderImpl(
            fileChildrenResultLoaderFile,
            fileChildrenResultLoaderContentResolver
        )
    }
}
