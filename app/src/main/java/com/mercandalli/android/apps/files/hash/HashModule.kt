package com.mercandalli.android.apps.files.hash

import android.content.Context

class HashModule(
        private val context: Context
) {

    fun createHashManager(): HashManager {
        return HashManagerImpl()
    }
}