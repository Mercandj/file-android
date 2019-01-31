@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.split_install

import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.tasks.Task

interface SplitInstallManager {

    fun startInstall(splitFeature: SplitFeature): Task<Int>?

    fun isInstalled(splitFeature: SplitFeature): Boolean

    fun getInstalled(): Set<String>

    fun deferredUninstall(splitFeature: SplitFeature)

    fun isUninstallTriggered(splitFeature: SplitFeature): Boolean

    fun cancelInstall(splitFeature: SplitFeature): Task<Void>?

    fun getSessionState(splitFeature: SplitFeature): Task<SplitInstallSessionState>?

    fun isDownloadingOrInstalling(splitFeature: SplitFeature): Boolean

    fun registerListener(splitFeature: SplitFeature, listener: SplitInstallListener)

    fun unregisterListener(splitFeature: SplitFeature, listener: SplitInstallListener)

    interface SplitInstallListener {

        fun onDownloading(splitFeature: SplitFeature)

        fun onInstalled(splitFeature: SplitFeature)

        fun onUninstalled(splitFeature: SplitFeature)

        fun onFailed(splitFeature: SplitFeature)
    }
}
