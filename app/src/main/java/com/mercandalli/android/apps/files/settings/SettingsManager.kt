package com.mercandalli.android.apps.files.settings

interface SettingsManager {

    fun isDeveloperMode(): Boolean

    fun setDeveloperMode(enable: Boolean)

    fun registerDeveloperModeListener(listener: DeveloperModeListener)

    fun unregisterDeveloperModeListener(listener: DeveloperModeListener)

    interface DeveloperModeListener {

        fun onDeveloperModeChanged()
    }
}