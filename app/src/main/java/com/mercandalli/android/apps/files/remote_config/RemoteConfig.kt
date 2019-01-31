@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.remote_config

interface RemoteConfig {

    /**
     * @return true if the [RemoteConfig] is initialized, false otherwise
     */
    fun isInitialized(): Boolean

    fun getSearchEnabled(): Boolean

    fun registerListener(listener: RemoteConfigListener)

    fun unregisterListener(listener: RemoteConfigListener)

    interface RemoteConfigListener {

        /***
         * is triggered when the [RemoteConfig] is initialized
         */
        fun onInitialized()
    }
}
