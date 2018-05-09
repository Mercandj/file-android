package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.FileCreatorManager
import java.io.IOException

class FileCreatorManagerAndroid(
        private val permissionManager: PermissionManager,
        private val mediaScanner: MediaScanner
) : FileCreatorManager {

    override fun create(path: String, name: String): Boolean {
        if (permissionManager.shouldRequestStoragePermission()) {
            return false
        }
        var pathToUse = path
        val len = path.length
        if (len < 1 || name.isEmpty()) {
            return false
        }
        if (pathToUse[len - 1] != '/') {
            pathToUse += "/"
        }
        if (!name.contains(".")) {
            val ioFile = java.io.File(pathToUse + name)
            if (ioFile.mkdir()) {
                mediaScanner.refresh(ioFile.absolutePath)
                mediaScanner.refresh(ioFile.parentFile.absolutePath)
                return true
            }
        } else {
            try {
                val ioFile = java.io.File(pathToUse + name)
                if (ioFile.createNewFile()) {
                    mediaScanner.refresh(ioFile.absolutePath)
                    mediaScanner.refresh(ioFile.parentFile.absolutePath)
                    return true
                }
            } catch (e: IOException) {
                return false
            }

        }
        return false
    }
}