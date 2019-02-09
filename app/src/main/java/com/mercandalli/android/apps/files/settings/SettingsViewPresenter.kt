package com.mercandalli.android.apps.files.settings

import com.mercandalli.android.apps.files.developer.DeveloperManager
import com.mercandalli.android.apps.files.product.ProductManager
import com.mercandalli.android.apps.files.remote_config.RemoteConfig

class SettingsViewPresenter(
    private val screen: SettingsViewContract.Screen,
    private val developerManager: DeveloperManager,
    private val productManager: ProductManager,
    private val remoteConfig: RemoteConfig
) : SettingsViewContract.UserAction {

    private val developerModeListener = createDeveloperModeListener()
    private val remoteConfigListener = createRemoteConfigListener()
    private val productListener = createProductListener()

    override fun onAttached() {
        developerManager.registerDeveloperModeListener(developerModeListener)
        productManager.registerProductListener(productListener)
        remoteConfig.registerListener(remoteConfigListener)
        populate()
    }

    override fun onDetached() {
        developerManager.unregisterDeveloperModeListener(developerModeListener)
        productManager.unregisterProductListener(productListener)
        remoteConfig.unregisterListener(remoteConfigListener)
    }

    private fun populate() {
        val developerMode = developerManager.isDeveloperMode()
        val searchEnabled = remoteConfig.getSearchEnabled()
        val fullVersionUnlocked = productManager.isFullVersionUnlocked()
        val list = ArrayList<Any>()
        list.add(SettingsAdapter.SettingsTheme())
        list.add(SettingsAdapter.SettingsStorage())
        if (!fullVersionUnlocked) {
            list.add(SettingsAdapter.SettingsStore())
        }
        if (searchEnabled || developerMode) {
            list.add(SettingsAdapter.SettingsDynamic())
        }
        if (developerMode) {
            list.add(SettingsAdapter.SettingsDeveloper())
        }
        list.add(SettingsAdapter.SettingsAbout())
        screen.populate(list)
    }

    private fun createDeveloperModeListener() = object : DeveloperManager.DeveloperModeListener {
        override fun onDeveloperModeChanged() {
            populate()
        }
    }

    private fun createRemoteConfigListener() = object : RemoteConfig.RemoteConfigListener {
        override fun onInitialized() {
            populate()
        }
    }

    private fun createProductListener() = object : ProductManager.ProductListener {
        override fun onProductChanged() {
            populate()
        }
    }
}
