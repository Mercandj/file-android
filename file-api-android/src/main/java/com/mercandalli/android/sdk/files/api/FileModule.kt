package com.mercandalli.android.sdk.files.api

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.N
import android.os.Environment
import android.support.annotation.RequiresApi
import android.support.v4.content.FileProvider
import android.widget.Toast
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.FileOpenManager
import java.io.File

class FileModule(
        private val context: Context,
        private val permissionRequestAddOn: PermissionRequestAddOn
) {

    fun provideFileManager(): FileManager {
        val permissionManager = PermissionManagerImpl(context, permissionRequestAddOn)
        val fileManagerAndroid = FileManagerAndroid(permissionManager)
        val fileObserver = RecursiveFileObserver(
                Environment.getExternalStorageDirectory().absolutePath,
                {
                    if (it != null && !it.endsWith("/null")) {
                        val path = File(it).parentFile.absolutePath
                        fileManagerAndroid.refresh(path)
                    }
                })
        fileObserver.startWatching()
        return fileManagerAndroid
    }

    fun provideFileOpenManager(): FileOpenManager {
        val addOn: FileOpenManagerAndroid.AddOn = object : FileOpenManagerAndroid.AddOn {
            override fun startActivity(path: String, mime: String) {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(getUriFromFile(context, File(path)), mime)
                startActivity(context, intent)
            }
        }
        return FileOpenManagerAndroid(addOn)
    }

    fun provideFileDeleteManager(): FileDeleteManager {
        val addOn: FileDeleteManagerAndroid.AddOn = object : FileDeleteManagerAndroid.AddOn {
            override fun refreshSystemMediaScanDataBase(path: String) {
                refreshSystemMediaScanDataBase(context, path)
            }
        }
        return FileDeleteManagerAndroid(addOn)
    }

    companion object {

        private fun getUriFromFile(
                context: Context,
                file: File): Uri {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getUriFromFileOverN(context, file)
            } else Uri.fromFile(file)
        }

        private fun getUriFromFileOverN(context: Context, file: File): Uri {
            return FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName + ".provider",
                    file)
        }

        private fun startActivity(
                context: Context,
                intent: Intent) {
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
                intent: Intent) {
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
        fun refreshSystemMediaScanDataBase(context: Context, docPath: String) {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(File(docPath))
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
        }
    }
}