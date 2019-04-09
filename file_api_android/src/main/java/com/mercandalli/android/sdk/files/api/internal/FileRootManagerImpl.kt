package com.mercandalli.android.sdk.files.api.internal

import android.os.Environment
import com.mercandalli.android.sdk.files.api.FileScopedStorageManager
import com.mercandalli.sdk.files.api.FileRootManager

class FileRootManagerImpl(
    private val fileScopedStorageManager: FileScopedStorageManager
) : FileRootManager {

    override fun getFileRootPath(): String {
        if (fileScopedStorageManager.isScopedStorage()) {
            return "content://com.android.externalstorage.documents/tree/primary%3A"
        }
        return Environment.getExternalStorageDirectory().absolutePath
    }
}
