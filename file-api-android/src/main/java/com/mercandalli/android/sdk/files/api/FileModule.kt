package com.mercandalli.android.sdk.files.api

import android.content.Context
import com.mercandalli.sdk.files.api.FileManager

class FileModule(
        private val context: Context,
        private val permissionRequestAddOn: PermissionRequestAddOn
) {

    fun provideFileManager(): FileManager {
        val permissionManager = PermissionManagerImpl(context, permissionRequestAddOn)
        return FileManagerAndroid(permissionManager)
    }

}