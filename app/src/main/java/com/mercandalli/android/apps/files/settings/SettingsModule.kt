package com.mercandalli.android.apps.files.settings

class SettingsModule {

    fun createSettingsManager(): SettingsManager {
        return SettingsManagerImpl()
    }
}