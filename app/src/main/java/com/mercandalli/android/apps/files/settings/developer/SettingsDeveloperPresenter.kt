package com.mercandalli.android.apps.files.settings.developer

import android.widget.ScrollView
import androidx.annotation.StringRes
import com.mercandalli.android.apps.files.settings.SettingsManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.theme.Theme

class SettingsDeveloperPresenter(
        private val screen: SettingsDeveloperContract.Screen,
        private val themeManager: ThemeManager,
        private val settingsManager: SettingsManager,
        private val addOn: AddOn
) : SettingsDeveloperContract.UserAction {

    private val themeListener = createThemeListener()
    private val appDeveloperListener = createAppDeveloperListener()

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        settingsManager.registerDeveloperModeListener(appDeveloperListener)
        updateTheme()
        syncDeveloperSection()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onVideoStartedCountClicked() {

    }

    override fun onActivationRowClicked() {
        settingsManager.setDeveloperMode(false)
        syncDeveloperSection()
    }

    override fun onDeveloperActivationCheckChanged(checked: Boolean) {
        settingsManager.setDeveloperMode(checked)
        syncDeveloperSection()
    }

    private fun syncDeveloperSection(isAppDeveloperModeEnabled: Boolean = settingsManager.isDeveloperMode()) {
        val visibility = if (isAppDeveloperModeEnabled) ScrollView.VISIBLE else ScrollView.GONE
        screen.setDeveloperSectionLabelVisibility(visibility)
        screen.setDeveloperSectionVisibility(visibility)
        screen.setDeveloperActivationChecked(isAppDeveloperModeEnabled)
        if (!isAppDeveloperModeEnabled) {
            return
        }
        syncDeveloperRemoteCountry()
    }

    private fun syncDeveloperRemoteCountry() {

    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
        screen.setTextSecondaryColorRes(theme.textSecondaryColorRes)
        screen.setSectionColor(theme.cardBackgroundColorRes)
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }

    private fun createAppDeveloperListener() = object : SettingsManager.DeveloperModeListener{
        override fun onDeveloperModeChanged() {
            syncDeveloperSection()
        }
    }

    interface AddOn {

        fun getString(@StringRes stringRes: Int): String

        fun getString(@StringRes stringRes: Int, value: Int): String

        fun getString(@StringRes stringRes: Int, value: String): String
    }
}