package com.mercandalli.android.apps.files.update

import android.content.SharedPreferences
import com.mercandalli.android.apps.files.version.VersionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UpdateManagerImpl(
    private val sharedPreferences: SharedPreferences,
    private val versionManager: VersionManager
) : UpdateManager {

    private val lastVersionName by lazy { sharedPreferences.getString(KEY_LAST_VERSION_NAME, "1.00.00") }
    private val firstRunInternal by lazy { sharedPreferences.getBoolean(KEY_FIRST_RUN, true) }
    private var firstLaunchAfterUpdate: Boolean? = null

    override fun isFirstRunAfterUpdate(): Boolean {
        if (firstLaunchAfterUpdate == null) {
            val versionName = versionManager.getVersionName()
            firstLaunchAfterUpdate = versionName != lastVersionName
            GlobalScope.launch(Dispatchers.Default) {
                sharedPreferences.edit().putString(KEY_LAST_VERSION_NAME, versionName).apply()
            }
        }
        return firstLaunchAfterUpdate!!
    }

    override fun isFirstRun(): Boolean {
        val firstRun = firstRunInternal
        GlobalScope.launch(Dispatchers.Default) {
            sharedPreferences.edit().putBoolean(KEY_FIRST_RUN, false).apply()
        }
        return firstRun
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "UpdateManager"
        private const val KEY_LAST_VERSION_NAME = "UpdateManager.KEY_LAST_VERSION_NAME"
        private const val KEY_FIRST_RUN = "UpdateManager.KEY_FIRST_RUN"
    }
}
