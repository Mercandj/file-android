@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_theme

import com.mercandalli.android.apps.files.theme.Theme
import com.mercandalli.android.apps.files.theme.ThemeManager

class SettingsThemePresenter(
    private val screen: SettingsThemeContract.Screen,
    private val themeManager: ThemeManager
) : SettingsThemeContract.UserAction {

    private val themeListener = createThemeListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        updateTheme()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onDarkThemeCheckBoxCheckedChanged(isChecked: Boolean) {
        themeManager.setDarkEnable(isChecked)
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setDarkThemeCheckBox(themeManager.isDarkEnable())
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setSectionColor(theme.cardBackgroundColorRes)
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }
}
