package com.mercandalli.android.apps.files.developer

import android.content.SharedPreferences

class DeveloperManagerImpl(
    private val sharedPreferences: SharedPreferences
) : DeveloperManager {

    private var isDeveloper = false
    private val developerModeListeners = ArrayList<DeveloperManager.DeveloperModeListener>()

    init {
        isDeveloper = sharedPreferences.getBoolean(SHARED_PREFERENCE_KEY_DEVELOPER_MODE, isDeveloper)
    }

    override fun isDeveloperMode() = isDeveloper

    override fun setDeveloperMode(enable: Boolean) {
        if (isDeveloper == enable) {
            return
        }
        isDeveloper = enable
        sharedPreferences.edit()
            .putBoolean(SHARED_PREFERENCE_KEY_DEVELOPER_MODE, isDeveloper)
            .apply()
        for (listener in developerModeListeners) {
            listener.onDeveloperModeChanged()
        }
    }

    override fun registerDeveloperModeListener(listener: DeveloperManager.DeveloperModeListener) {
        if (developerModeListeners.contains(listener)) {
            return
        }
        developerModeListeners.add(listener)
    }

    override fun unregisterDeveloperModeListener(listener: DeveloperManager.DeveloperModeListener) {
        developerModeListeners.remove(listener)
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "DeveloperManager"
        private const val SHARED_PREFERENCE_KEY_DEVELOPER_MODE = "developer-mode"
    }
}
