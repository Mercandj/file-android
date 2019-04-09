package com.mercandalli.android.sdk.files.api.internal

import com.mercandalli.sdk.files.api.FileParentManager
import java.lang.StringBuilder

internal class FileParentManagerAndroid : FileParentManager {

    override fun getParentPath(
        path: String
    ): String? {
        return if (
            path.startsWith("content://")
        ) {
            if (!path.contains("%2F")) {
                "content://com.android.externalstorage.documents/tree/primary%3A"
            } else {
                val split = path.split("%2F")
                val parentPath = StringBuilder()
                for (i in 0..split.size - 2) {
                    parentPath.append(split[i])
                    if (i < split.size - 2) {
                        parentPath.append("%2F")
                    }
                }
                parentPath.toString()
            }
        } else {
            val ioFile = java.io.File(path)
            val parentFile = ioFile.parentFile ?: return null
            return parentFile.absolutePath
        }
    }
}
