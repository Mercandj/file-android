@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_store

import android.app.Activity
import com.mercandalli.android.apps.files.product.ProductManager
import com.mercandalli.android.apps.files.theme.DarkTheme
import com.mercandalli.android.apps.files.theme.Theme
import com.mercandalli.android.apps.files.theme.ThemeManager

class SettingsStorePresenter(
    private val screen: SettingsStoreContract.Screen,
    private val productManager: ProductManager,
    private val themeManager: ThemeManager
) : SettingsStoreContract.UserAction {

    private val themeListener = createThemeListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        updateTheme()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onRowClicked(activity: Activity) {
        val activityContainer = object : ProductManager.ActivityContainer {
            override fun get() = activity
        }
        productManager.purchaseFullVersion(activityContainer)
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setSectionColor(theme.cardBackgroundColorRes)
        screen.setPromotionGradient(theme is DarkTheme)
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }
}
