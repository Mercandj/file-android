package com.mercandalli.android.apps.files.theme

import android.content.Context

class ThemeModule {

    fun provideThemeManager(
            context: Context): ThemeManager {
        val sharedPreferences = context.getSharedPreferences(
                ThemeManagerImpl.PREFERENCE_NAME, Context.MODE_PRIVATE)
        return ThemeManagerImpl(sharedPreferences)
    }
}