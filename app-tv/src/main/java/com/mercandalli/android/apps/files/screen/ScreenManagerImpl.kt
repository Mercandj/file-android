package com.mercandalli.android.apps.files.screen

import android.content.Context
import com.mercandalli.android.apps.files.permission.PermissionActivity

class ScreenManagerImpl(
    private val context: Context
) : ScreenManager {

    override fun startPermission() {
        PermissionActivity.start(context)
    }
}
