package com.mercandalli.android.apps.files.product

import com.mercandalli.android.apps.files.developer.DeveloperManager
import com.mercandalli.android.apps.files.remote_config.RemoteConfig
import com.mercandalli.android.sdk.purchase.PurchaseDetails
import com.mercandalli.android.sdk.purchase.PurchaseManager

internal class ProductManagerImpl(
    private val developerManager: DeveloperManager,
    private val purchaseManager: PurchaseManager,
    private val remoteConfig: RemoteConfig
) : ProductManager {

    private val productListeners = ArrayList<ProductManager.ProductListener>()
    private val developerListener = createDeveloperListener()

    override fun initialize() {
        purchaseManager.initialize()
        val purchaseListener = createPurchaseListener()
        purchaseManager.registerListener(purchaseListener)
        developerManager.registerDeveloperModeListener(developerListener)
    }

    override fun isFullVersionUnlocked(): Boolean {
        val developerMode = developerManager.isDeveloperMode()
        if (developerMode) {
            return true
        }
        val fullVersionSku = getFullVersionSku() ?: return true
        val purchased = purchaseManager.isPurchased(fullVersionSku)
        if (purchased) {
            return true
        }
        return !purchaseManager.isPurchasedEmpty()
    }

    override fun purchaseFullVersion(activityContainer: ProductManager.ActivityContainer) {
        val fullVersionSku = getFullVersionSku() ?: return
        val activityContainerPurchase = object : PurchaseManager.ActivityContainer {
            override fun get() = activityContainer.get()
        }
        purchaseManager.purchase(
            activityContainerPurchase,
            fullVersionSku,
            PurchaseManager.SUBS
        )
    }

    override fun registerProductListener(listener: ProductManager.ProductListener) {
        if (productListeners.contains(listener)) {
            return
        }
        productListeners.add(listener)
    }

    override fun unregisterProductListener(listener: ProductManager.ProductListener) {
        productListeners.remove(listener)
    }

    private fun getFullVersionSku(): String? {
        val fullVersionSku = remoteConfig.getSubscriptionFullVersionSku()
        if (fullVersionSku == "") {
            return null
        }
        return fullVersionSku
    }

    private fun createPurchaseListener() = object : PurchaseManager.Listener {
        override fun onSkuDetailsChanged(purchaseDetails: PurchaseDetails) {
        }

        override fun onPurchasedChanged() {
            for (listener in productListeners) {
                listener.onProductChanged()
            }
        }
    }

    private fun createDeveloperListener() = object : DeveloperManager.DeveloperModeListener {
        override fun onDeveloperModeChanged() {
            for (listener in productListeners) {
                listener.onProductChanged()
            }
        }
    }
}
