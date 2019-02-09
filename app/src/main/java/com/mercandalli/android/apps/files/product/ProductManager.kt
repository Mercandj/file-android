package com.mercandalli.android.apps.files.product

import android.app.Activity

interface ProductManager {

    fun initialize()

    fun isFullVersionUnlocked(): Boolean

    fun purchaseFullVersion(activityContainer: ActivityContainer)

    fun registerProductListener(listener: ProductListener)

    fun unregisterProductListener(listener: ProductListener)

    interface ActivityContainer {

        fun get(): Activity
    }

    interface ProductListener {

        fun onProductChanged()
    }
}
