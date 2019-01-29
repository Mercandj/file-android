package com.mercandalli.android.apps.files.screen

import android.content.Context
import com.mercandalli.android.apps.files.permission.PermissionActivity
import com.mercandalli.android.apps.files.search.SearchActivity

class ScreenManagerImpl(
    private val context: Context
) : ScreenManager {

    override fun startPermission() {
        PermissionActivity.start(context)
    }

    override fun startSearch() {
        SearchActivity.start(context)
    }
}
