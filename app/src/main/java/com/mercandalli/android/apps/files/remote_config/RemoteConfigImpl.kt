@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.remote_config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.mercandalli.android.apps.files.BuildConfig
import com.mercandalli.android.apps.files.main_thread.MainThreadPost
import com.mercandalli.android.apps.files.update.UpdateManager

import java.util.ArrayList
import java.util.HashMap

/**
 * This class is used to get the remote configuration from firebase
 */
internal class RemoteConfigImpl(
    updateManager: UpdateManager,
    private val mainThreadPost: MainThreadPost
) : RemoteConfig {

    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private val listeners = ArrayList<RemoteConfig.RemoteConfigListener>()

    private var isInitializedInternal = false

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()
        firebaseRemoteConfig.setConfigSettings(configSettings)

        val defaultMap = HashMap<String, Any>()
        defaultMap[FIREBASE_KEY_SEARCH_ENABLED] = defaultSearchEnabled
        firebaseRemoteConfig.setDefaults(defaultMap)

        val firstLaunchAfterUpdate = updateManager.isFirstRunAfterUpdate()
        val bypassCache = BuildConfig.DEBUG || firstLaunchAfterUpdate
        (if (bypassCache) firebaseRemoteConfig.fetch(0) else firebaseRemoteConfig.fetch())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseRemoteConfig.activateFetched()
                    isInitializedInternal = true
                    notifyInitialized()
                }
            }
    }

    override fun isInitialized() = isInitializedInternal
    override fun getSearchEnabled() = firebaseRemoteConfig.getBoolean(FIREBASE_KEY_SEARCH_ENABLED)

    override fun registerListener(listener: RemoteConfig.RemoteConfigListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    override fun unregisterListener(listener: RemoteConfig.RemoteConfigListener) {
        listeners.remove(listener)
    }

    private fun notifyInitialized() {
        if (!mainThreadPost.isOnMainThread()) {
            mainThreadPost.post(Runnable { this.notifyInitialized() })
            return
        }
        for (listener in listeners) {
            listener.onInitialized()
        }
    }

    companion object {
        private const val FIREBASE_KEY_SEARCH_ENABLED = "search_enabled"
        private const val defaultSearchEnabled = false
    }
}
