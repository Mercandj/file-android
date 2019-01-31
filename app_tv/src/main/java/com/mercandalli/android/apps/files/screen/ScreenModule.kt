package com.mercandalli.android.apps.files.screen

import android.content.Context

class ScreenModule(
    private val context: Context
) {

    fun createScreenManager(): ScreenManager {
        return ScreenManagerImpl(
            context
        )
    }
}
