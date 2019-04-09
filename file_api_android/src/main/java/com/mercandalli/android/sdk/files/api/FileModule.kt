@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.sdk.files.api

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.N
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import android.widget.Toast
import com.mercandalli.android.sdk.files.api.internal.FileRootManagerImpl
import com.mercandalli.android.sdk.files.api.internal.FileScopedStorageManagerImpl
import com.mercandalli.android.sdk.files.api.internal.PermissionModule
import com.mercandalli.android.sdk.files.api.internal.FileManagerAndroid
import com.mercandalli.android.sdk.files.api.internal.FileOpenManagerAndroid
import com.mercandalli.android.sdk.files.api.internal.FileParentManagerAndroid
import com.mercandalli.android.sdk.files.api.internal.FileZipManagerAndroid
import com.mercandalli.android.sdk.files.api.internal.FileRenameManagerAndroid
import com.mercandalli.android.sdk.files.api.internal.FileDeleteManagerAndroid
import com.mercandalli.android.sdk.files.api.internal.FileSizeManagerAndroid
import com.mercandalli.android.sdk.files.api.internal.MediaScannerAndroid
import com.mercandalli.android.sdk.files.api.internal.FileCreatorManagerAndroid
import com.mercandalli.android.sdk.files.api.internal.FileCopyCutManagerAndroid
import com.mercandalli.android.sdk.files.api.internal.FileSearchManagerAndroid
import com.mercandalli.android.sdk.files.api.internal.FileShareManagerAndroid
import com.mercandalli.android.sdk.files.api.internal.file_children.FileChildrenModule
import com.mercandalli.sdk.files.api.FileRootManager
import com.mercandalli.sdk.files.api.MediaScanner
import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.FileCreatorManager
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileParentManager
import com.mercandalli.sdk.files.api.FileOpenManager
import com.mercandalli.sdk.files.api.FileRenameManager
import com.mercandalli.sdk.files.api.FileSortManager
import com.mercandalli.sdk.files.api.FileSortManagerImpl
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileSizeManager
import com.mercandalli.sdk.files.api.FileShareManager
import com.mercandalli.sdk.files.api.FileSearchManager
import com.mercandalli.sdk.files.api.FileZipManager
import java.io.File

class FileModule(
    private val context: Context,
    private val permissionRequestAddOn: PermissionRequestAddOn
) {

    private val mediaScannerInternal by lazy { createMediaScanner() }
    private val fileRootManagerInternal by lazy { createFileRootManager() }
    private val fileScopedStorageManagerInternal by lazy { createFileScopedStorageManager() }
    private val permissionManagerInternal by lazy { createPermissionManager() }
    private val fileZipManagerInternal by lazy { createFileZipManager() }

    fun getMediaScanner() = mediaScannerInternal

    fun getPermissionManager() = permissionManagerInternal

    fun createFileManager(): FileManager {
        val fileManager = FileManagerAndroid(permissionManagerInternal)
        val fileObserver = RecursiveFileObserver(
            fileRootManagerInternal.getFileRootPath()
        ) {
            if (it != null && !it.endsWith("/null")) {
                val path = File(it).parentFile.absolutePath
                fileManager.refresh(path)
            }
        }
        mediaScannerInternal.addListener(object : MediaScanner.RefreshListener {
            override fun onContentChanged(path: String) {
                fileManager.refresh(path)
            }
        })
        fileObserver.startWatching()
        return fileManager
    }

    fun createFileChildrenManager(): FileChildrenManager {
        return FileChildrenModule(
            context,
            mediaScannerInternal,
            permissionManagerInternal,
            fileRootManagerInternal
        ).createFileChildrenManager()
    }

    fun createFileOpenManager(): FileOpenManager {
        val addOn = object : FileOpenManagerAndroid.AddOn {
            override fun startActivity(path: String, mime: String) {
                val uri = getUriFromFilePath(
                    context,
                    path
                )
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(uri, mime)
                if (context !is Activity) {
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(context, intent)
            }
        }
        return FileOpenManagerAndroid(
            fileZipManagerInternal,
            addOn
        )
    }

    fun createFileDeleteManager(): FileDeleteManager = FileDeleteManagerAndroid(
        mediaScannerInternal
    )

    fun createFileCopyCutManager(): FileCopyCutManager = FileCopyCutManagerAndroid(
        mediaScannerInternal
    )

    fun createFileCreatorManager(): FileCreatorManager = FileCreatorManagerAndroid(
        permissionManagerInternal,
        mediaScannerInternal
    )

    fun createFileParentManager(): FileParentManager {
        return FileParentManagerAndroid()
    }

    fun createFileRenameManager(): FileRenameManager = FileRenameManagerAndroid(
        mediaScannerInternal
    )

    fun getFileRootManager(): FileRootManager {
        return FileRootManagerImpl(
            fileScopedStorageManagerInternal
        )
    }

    fun getFileScopedStorageManager(): FileScopedStorageManager {
        return fileScopedStorageManagerInternal
    }

    fun createFileSearchManager(): FileSearchManager {
        return FileSearchManagerAndroid(
            fileRootManagerInternal
        )
    }

    fun createFileShareManager(): FileShareManager {
        val addOn = object : FileShareManagerAndroid.AddOn {
            override fun startActivity(path: String, mime: String) {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(getUriFromIOFile(context, File(path)), mime)
                if (context !is Activity) {
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(context, intent)
            }
        }
        return FileShareManagerAndroid(addOn)
    }

    fun createFileSizeManager(): FileSizeManager {
        val fileSizeManager = FileSizeManagerAndroid(
            permissionManagerInternal
        )
        mediaScannerInternal.addListener(object : MediaScanner.RefreshListener {
            override fun onContentChanged(path: String) {
                fileSizeManager.loadSize(path, true)
            }
        })
        return fileSizeManager
    }

    fun createFileSortManager(): FileSortManager = FileSortManagerImpl()

    private fun createMediaScanner(): MediaScanner {
        val addOn = object : MediaScannerAndroid.AddOn {
            override fun refreshSystemMediaScanDataBase(path: String) {
                refreshSystemMediaScanDataBase(context, path)
            }
        }
        return MediaScannerAndroid(
            addOn
        )
    }

    private fun createFileRootManager(): FileRootManager {
        return FileRootManagerImpl(
            fileScopedStorageManagerInternal
        )
    }

    private fun createFileScopedStorageManager(): FileScopedStorageManager {
        return FileScopedStorageManagerImpl(context)
    }

    private fun createPermissionManager(): PermissionManager {
        val permissionModule = PermissionModule(
            context,
            fileScopedStorageManagerInternal,
            permissionRequestAddOn
        )
        return permissionModule.createPermissionManager()
    }

    private fun createFileZipManager(): FileZipManager {
        return FileZipManagerAndroid(
            mediaScannerInternal
        )
    }

    companion object {

        private fun getUriFromFilePath(
            context: Context,
            filePath: String
        ): Uri {
            return if (filePath.startsWith("content://")) {
                Uri.parse(filePath)
            } else {
                getUriFromIOFile(
                    context,
                    File(filePath)
                )
            }
        }

        private fun getUriFromIOFile(
            context: Context,
            ioFile: File
        ): Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getUriFromFileOverN(context, ioFile)
        } else {
            Uri.fromFile(ioFile)
        }

        private fun getUriFromFileOverN(
            context: Context,
            file: File
        ): Uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )

        private fun startActivity(
            context: Context,
            intent: Intent
        ) {
            try {
                if (Build.VERSION.SDK_INT >= N) {
                    startActivityOverN(context, intent)
                } else {
                    context.startActivity(intent)
                }
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Oops, there is an error. Try with \"Open as...\"",
                    Toast.LENGTH_SHORT).show()
            }
        }

        @RequiresApi(api = N)
        private fun startActivityOverN(
            context: Context,
            intent: Intent
        ) {
            try {
                context.startActivity(intent)
            } catch (e: Exception) { // Catch a FileUriExposedException.
                // Test on KitKat if your replace Exception by FileUriExposedException.
                Toast.makeText(context, "Oops, there is an error.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        /**
         * @param context : it is the reference where this method get called
         * @param docPath : absolute path of file for which broadcast will be send to refresh media database
         */
        private fun refreshSystemMediaScanDataBase(context: Context, docPath: String) {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(File(docPath))
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
        }
    }
}
