package com.mercandalli.android.apps.files.settings

class SettingsManagerImpl : SettingsManager {

    private var isDeveloper = false

    override fun isDeveloperMode() = isDeveloper

    override fun setDeveloperMode(enable: Boolean) {
        isDeveloper = enable
    }
}