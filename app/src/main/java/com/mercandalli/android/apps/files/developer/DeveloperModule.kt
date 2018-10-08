package com.mercandalli.android.apps.files.developer

import android.content.Context

class DeveloperModule(
        private val context: Context
) {

    fun createDeveloperManager(): DeveloperManager {
        val sharedPreferences = context.getSharedPreferences(
                DeveloperManagerImpl.PREFERENCE_NAME,
                Context.MODE_PRIVATE
        )
        return DeveloperManagerImpl(
                sharedPreferences
        )
    }
}