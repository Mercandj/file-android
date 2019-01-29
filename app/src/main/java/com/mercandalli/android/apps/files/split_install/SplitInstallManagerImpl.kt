@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.split_install

import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.google.android.play.core.tasks.Task
import java.lang.IllegalStateException
import java.util.Arrays

class SplitInstallManagerImpl(
    private val splitInstallManager: com.google.android.play.core.splitinstall.SplitInstallManager,
    private val splitUninstallStorage: SplitUninstallStorage,
    private val moduleAppSearchDynamic: String
) : SplitInstallManager {

    private val sessionIds = HashMap<SplitFeature, Int>()
    private val listeners = HashMap<SplitFeature, ArrayList<SplitInstallManager.SplitInstallListener>>()
    private val downloadingOrInstalling = HashMap<SplitFeature, Boolean>()

    init {
        splitInstallManager.registerListener(createSplitInstallStateUpdatedListener())
    }

    override fun startInstall(splitFeature: SplitFeature): Task<Int>? {
        if (isInstalled(splitFeature)) {
            return null
        }
        if (isDownloadingOrInstalling(splitFeature)) {
            return null
        }
        val featureModuleName = getFeatureModuleName(splitFeature)
        splitUninstallStorage.setUninstallTrigger(featureModuleName, false)
        val request = SplitInstallRequest.newBuilder()
            .addModule(featureModuleName)
            .build()
        return splitInstallManager.startInstall(request)
            // When the platform accepts your request to download
            // an on demand module, it binds it to the following session ID.
            // You use this ID to track further status updates for the request.
            .addOnSuccessListener {
                sessionIds[splitFeature] = it
            }
    }

    override fun isInstalled(splitFeature: SplitFeature): Boolean {
        val featureModuleName = getFeatureModuleName(splitFeature)
        return splitInstallManager.installedModules.contains(featureModuleName)
    }

    override fun getInstalled(): Set<String> = splitInstallManager.installedModules

    override fun deferredUninstall(splitFeature: SplitFeature) {
        val featureModuleName = getFeatureModuleName(splitFeature)
        splitUninstallStorage.setUninstallTrigger(featureModuleName, true)
        splitInstallManager.deferredUninstall(Arrays.asList(featureModuleName))
            .addOnCompleteListener {
                notifyUninstalled(splitFeature)
            }
    }

    override fun isUninstallTriggered(splitFeature: SplitFeature): Boolean {
        val featureModuleName = getFeatureModuleName(splitFeature)
        return splitUninstallStorage.isUninstallTrigger(featureModuleName)
    }

    override fun cancelInstall(splitFeature: SplitFeature): Task<Void>? {
        if (!sessionIds.containsKey(splitFeature)) {
            return null
        }
        val sessionId = sessionIds[splitFeature]!!
        return splitInstallManager.cancelInstall(sessionId)
    }

    override fun getSessionState(splitFeature: SplitFeature): Task<SplitInstallSessionState>? {
        if (!sessionIds.containsKey(splitFeature)) {
            return null
        }
        val sessionId = sessionIds[splitFeature]!!
        return splitInstallManager.getSessionState(sessionId)
    }

    override fun isDownloadingOrInstalling(splitFeature: SplitFeature): Boolean {
        if (!downloadingOrInstalling.containsKey(splitFeature)) {
            return false
        }
        return downloadingOrInstalling[splitFeature]!!
    }

    override fun registerListener(
        splitFeature: SplitFeature,
        listener: SplitInstallManager.SplitInstallListener
    ) {
        if (listeners.containsKey(splitFeature)) {
            val list = listeners[splitFeature]!!
            if (list.contains(listener)) {
                return
            }
        } else {
            listeners[splitFeature] = ArrayList()
        }
        listeners[splitFeature]!!.add(listener)
    }

    override fun unregisterListener(
        splitFeature: SplitFeature,
        listener: SplitInstallManager.SplitInstallListener
    ) {
        if (!listeners.containsKey(splitFeature)) {
            return
        }
        listeners[splitFeature]!!.remove(listener)
    }

    private fun createSplitInstallStateUpdatedListener() = SplitInstallStateUpdatedListener { state ->
        state.moduleNames().forEach { featureModuleName ->
            val splitFeature = getSplitFeature(featureModuleName)
            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    downloadingOrInstalling[splitFeature] = true
                    notifyDownloading(splitFeature)
                }
                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                    downloadingOrInstalling[splitFeature] = false
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    splitUninstallStorage.setUninstallTrigger(featureModuleName, false)
                    downloadingOrInstalling[splitFeature] = false
                    notifyInstalled(splitFeature)
                }
                SplitInstallSessionStatus.INSTALLING -> {
                    splitUninstallStorage.setUninstallTrigger(featureModuleName, false)
                    downloadingOrInstalling[splitFeature] = true
                }
                SplitInstallSessionStatus.FAILED -> {
                    downloadingOrInstalling[splitFeature] = false
                }
            }
        }
    }

    private fun notifyDownloading(splitFeature: SplitFeature) {
        if (!listeners.containsKey(splitFeature)) {
            return
        }
        for (listener in listeners[splitFeature]!!) {
            listener.onDownloading(splitFeature)
        }
    }

    private fun notifyInstalled(splitFeature: SplitFeature) {
        if (!listeners.containsKey(splitFeature)) {
            return
        }
        for (listener in listeners[splitFeature]!!) {
            listener.onInstalled(splitFeature)
        }
    }

    private fun notifyUninstalled(splitFeature: SplitFeature) {
        if (!listeners.containsKey(splitFeature)) {
            return
        }
        for (listener in listeners[splitFeature]!!) {
            listener.onUninstalled(splitFeature)
        }
    }

    private fun getFeatureModuleName(splitFeature: SplitFeature): String {
        when (splitFeature) {
            SplitFeature.Search -> {
                return moduleAppSearchDynamic
            }
        }
    }

    private fun getSplitFeature(name: String): SplitFeature {
        when (name) {
            moduleAppSearchDynamic -> {
                return SplitFeature.Search
            }
        }
        throw IllegalStateException()
    }
}
