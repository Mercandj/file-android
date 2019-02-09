package com.mercandalli.android.apps.files.product

import com.mercandalli.android.apps.files.remote_config.RemoteConfig
import com.mercandalli.android.sdk.purchase.PurchaseDetails
import com.mercandalli.android.sdk.purchase.PurchaseManager

internal class ProductManagerImpl(
    private val purchaseManager: PurchaseManager,
    private val remoteConfig: RemoteConfig
) : ProductManager {

    private val productListeners = ArrayList<ProductManager.ProductListener>()

    override fun initialize() {
        purchaseManager.initialize()
        val purchaseListener = createPurchaseListener()
        purchaseManager.registerListener(purchaseListener)
    }

    override fun isFullVersionUnlocked(): Boolean {
        val fullVersionSku = getFullVersionSku()
        return purchaseManager.isPurchased(fullVersionSku)
    }

    override fun purchaseFullVersion(activityContainer: ProductManager.ActivityContainer) {
        val fullVersionSku = getFullVersionSku()
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

    private fun getFullVersionSku(): String {
        return remoteConfig.getSubscriptionFullVersionSku()
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
}
