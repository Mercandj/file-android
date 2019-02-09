package com.mercandalli.android.apps.files.product

import android.content.Context
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.android.sdk.purchase.PurchaseManager
import com.mercandalli.android.sdk.purchase.PurchaseModule

class ProductModule(
    private val context: Context
) {

    fun createProductManager(): ProductManager {
        val developerManager = ApplicationGraph.getDeveloperManager()
        val purchaseManager = createPurchaseManager()
        val remoteConfig = ApplicationGraph.getRemoteConfig()
        return ProductManagerImpl(
            developerManager,
            purchaseManager,
            remoteConfig
        )
    }

    private fun createPurchaseManager(): PurchaseManager {
        val purchaseModule = PurchaseModule(context)
        return purchaseModule.createInAppManager()
    }
}
