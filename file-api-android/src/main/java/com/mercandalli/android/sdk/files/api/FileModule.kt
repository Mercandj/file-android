package com.mercandalli.android.sdk.files.api

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.N
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import android.widget.Toast
import com.mercandalli.sdk.files.api.FileZipManager
import com.mercandalli.sdk.files.api.MediaScanner
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileOpenManager
import com.mercandalli.sdk.files.api.FileCreatorManager
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileSortManager
import com.mercandalli.sdk.files.api.FileSizeManager
import com.mercandalli.sdk.files.api.FileRenameManager
import com.mercandalli.sdk.files.api.FileSortManagerImpl
import com.mercandalli.sdk.files.api.FileShareManager
import java.io.File

class FileModule(
    private val context: Context,
    private val permissionRequestAddOn: PermissionRequestAddOn
) {

    private val mediaScannerInternal: MediaScanner by lazy {
        val addOn = object : MediaScannerAndroid.AddOn {
            override fun refreshSystemMediaScanDataBase(path: String) {
                refreshSystemMediaScanDataBase(context, path)
            }
        }
        MediaScannerAndroid(addOn)
    }

    private val permissionManager: PermissionManager by lazy { PermissionManagerImpl(context, permissionRequestAddOn) }
    private val fileZipManagerInternal: FileZipManager by lazy { FileZipManagerAndroid(mediaScannerInternal) }

    fun getMediaScanner() = mediaScannerInternal

    fun createFileManager(): FileManager {
        val fileManager = FileManagerAndroid(permissionManager)
        val fileObserver = RecursiveFileObserver(
            Environment.getExternalStorageDirectory().absolutePath
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
        val fileChildrenManager = FileChildrenManagerAndroid(permissionManager)
        val fileObserver = RecursiveFileObserver(
            Environment.getExternalStorageDirectory().absolutePath
        ) {
            if (it != null && !it.endsWith("/null")) {
                val path = File(it).parentFile.absolutePath
                fileChildrenManager.refresh(path)
            }
        }
        mediaScannerInternal.addListener(object : MediaScanner.RefreshListener {
            override fun onContentChanged(path: String) {
                fileChildrenManager.refresh(path)
            }
        })
        fileObserver.startWatching()
        return fileChildrenManager
    }

    fun createFileOpenManager(): FileOpenManager {
        val addOn = object : FileOpenManagerAndroid.AddOn {
            override fun startActivity(path: String, mime: String) {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(getUriFromFile(context, File(path)), mime)
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
        permissionManager,
        mediaScannerInternal
    )

    fun createFileRenameManager(): FileRenameManager = FileRenameManagerAndroid(
        mediaScannerInternal
    )

    fun createFileShareManager(): FileShareManager {
        val addOn = object : FileShareManagerAndroid.AddOn {
            override fun startActivity(path: String, mime: String) {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(getUriFromFile(context, File(path)), mime)
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
            permissionManager
        )
        mediaScannerInternal.addListener(object : MediaScanner.RefreshListener {
            override fun onContentChanged(path: String) {
                fileSizeManager.loadSize(path, true)
            }
        })
        return fileSizeManager
    }

    fun createFileSortManager(): FileSortManager = FileSortManagerImpl()

    companion object {

        private fun getUriFromFile(
            context: Context,
            file: File
        ): Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getUriFromFileOverN(context, file)
        } else {
            Uri.fromFile(file)
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
