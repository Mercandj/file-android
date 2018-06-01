package com.mercandalli.android.apps.files.settings

interface SettingsManager {

    fun isDeveloperMode(): Boolean

    fun setDeveloperMode(enable: Boolean)
}