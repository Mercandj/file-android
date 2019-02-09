@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_full_version

import com.mercandalli.android.apps.files.theme.DarkTheme
import com.mercandalli.android.apps.files.theme.Theme
import com.mercandalli.android.apps.files.theme.ThemeManager

class SettingsFullVersionPresenter(
    private val screen: SettingsFullVersionContract.Screen,
    private val themeManager: ThemeManager
) : SettingsFullVersionContract.UserAction {

    private val themeListener = createThemeListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        updateTheme()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
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
