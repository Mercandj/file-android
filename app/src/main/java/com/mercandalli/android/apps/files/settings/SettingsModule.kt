package com.mercandalli.android.apps.files.settings

class SettingsModule {

    fun provideSettingsManager(): SettingsManager {
        return SettingsManagerImpl()
    }
}