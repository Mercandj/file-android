package com.mercandalli.android.sdk.files.api.internal

import com.mercandalli.android.sdk.files.api.PermissionManager
import com.mercandalli.sdk.files.api.FileCreatorManager
import com.mercandalli.sdk.files.api.MediaScanner
import java.io.IOException

internal class FileCreatorManagerAndroid(
    private val permissionManager: PermissionManager,
    private val mediaScanner: MediaScanner,
    private val addOn: AddOn
) : FileCreatorManager {

    override fun create(
        parentPath: String,
        name: String
    ) {
        if (permissionManager.requestStoragePermissionIfRequired()) {
            return
        }
        if (
            parentPath.startsWith("content://")
        ) {
            val succeeded = if (name.contains(".")) {
                addOn.createFileFromContentResolver(
                    parentPath,
                    name
                )
            } else {
                addOn.createDirectoryFromContentResolver(
                    parentPath,
                    name
                )
            }
            if (succeeded) {
                mediaScanner.refresh(parentPath)
            }
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
            if (!ioFile.mkdir()) {
                return
            }
            mediaScanner.refresh(ioFile.absolutePath)
            mediaScanner.refresh(ioFile.parentFile.absolutePath)
        } else {
            try {
                val ioFile = java.io.File(pathToUse + name)
                if (!ioFile.createNewFile()) {
                    return
                }
                mediaScanner.refresh(ioFile.absolutePath)
                mediaScanner.refresh(ioFile.parentFile.absolutePath)
                return
            } catch (e: IOException) {
                return
            }
        }
        return
    }

    interface AddOn {

        fun createFileFromContentResolver(
            parentPath: String,
            name: String
        ): Boolean

        fun createDirectoryFromContentResolver(
            parentPath: String,
            name: String
        ): Boolean
    }
}
