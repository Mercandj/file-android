package com.mercandalli.android.apps.files.toast

import android.content.Context

class ToastModule(
    val context: Context
) {

    fun createToastManager(): ToastManager {
        return ToastManagerImpl(
            context.applicationContext
        )
    }
}
