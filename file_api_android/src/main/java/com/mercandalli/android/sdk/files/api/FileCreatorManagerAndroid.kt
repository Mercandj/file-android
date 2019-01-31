package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.FileCreatorManager
import com.mercandalli.sdk.files.api.MediaScanner
import java.io.IOException

class FileCreatorManagerAndroid(
    private val permissionManager: PermissionManager,
    private val mediaScanner: MediaScanner
) : FileCreatorManager {

    override fun create(parentPath: String, name: String) {
        if (permissionManager.shouldRequestStoragePermission()) {
            return
        }
        var pathToUse = parentPath
        val len = parentPath.length
        if (len < 1 || name.isEmpty()) {
            return
        }
        if (pathToUse[len - 1] != '/') {
            pathToUse += "/"
        }
        if (!name.contains(".")) {
            val ioFile = java.io.File(pathToUse + name)
            if (ioFile.mkdir()) {
                mediaScanner.refresh(ioFile.absolutePath)
                mediaScanner.refresh(ioFile.parentFile.absolutePath)
                return
            }
        } else {
            try {
                val ioFile = java.io.File(pathToUse + name)
                if (ioFile.createNewFile()) {
                    mediaScanner.refresh(ioFile.absolutePath)
                    mediaScanner.refresh(ioFile.parentFile.absolutePath)
                    return
                }
            } catch (e: IOException) {
                return
            }
        }
    }
}
