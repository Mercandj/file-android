package com.mercandalli.android.apps.files.settings

class SettingsManagerImpl : SettingsManager {

    private var isDeveloper = false

    override fun isDeveloperMode(): Boolean {
        return isDeveloper
    }

    override fun setDeveloperMode(enable: Boolean) {
        isDeveloper = enable
    }
}