package com.mercandalli.android.apps.files.settings

class SettingsManagerImpl : SettingsManager {

    private var isDeveloper = false
    private val developerModeListeners = ArrayList<SettingsManager.DeveloperModeListener>()

    override fun isDeveloperMode() = isDeveloper

    override fun setDeveloperMode(enable: Boolean) {
        if (isDeveloper == enable) {
            return
        }
        isDeveloper = enable
        for (listener in developerModeListeners) {
            listener.onDeveloperModeChanged()
        }
    }

    override fun registerDeveloperModeListener(listener: SettingsManager.DeveloperModeListener) {
        if (developerModeListeners.contains(listener)) {
            return
        }
        developerModeListeners.add(listener)
    }

    override fun unregisterDeveloperModeListener(listener: SettingsManager.DeveloperModeListener) {
        developerModeListeners.remove(listener)
    }
}