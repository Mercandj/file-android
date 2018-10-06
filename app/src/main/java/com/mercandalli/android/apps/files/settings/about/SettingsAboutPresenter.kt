package com.mercandalli.android.apps.files.settings.about

import com.mercandalli.android.apps.files.theme.DarkTheme
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.version.VersionManager

class SettingsAboutPresenter(
        private val screen: SettingsAboutContract.Screen,
        versionManager: VersionManager,
        private val themeManager: ThemeManager
) : SettingsAboutContract.UserAction {

    private val themeListener = createThemeListener()

    init {
        val versionName = versionManager.getVersionName()
        screen.showVersionName(versionName)
    }

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        syncWithCurrentTheme()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onRateClicked() {
        screen.openUrl(PLAY_STORE_URL_FILESPACE)
    }

    override fun onTeamAppsClicked() {
        screen.openUrl(PLAY_STORE_URL_TEAM_MERCAN)
    }

    override fun onThemeRowClicked(checked: Boolean) {
        themeManager.setDarkEnable(checked)
    }

    override fun onThemeCheckboxCheckedChange(checked: Boolean) {
        themeManager.setDarkEnable(checked)
    }

    private fun syncWithCurrentTheme() {
        val theme = themeManager.getTheme()
        screen.setCardBackgroundColorRes(theme.cardBackgroundColorRes)
        screen.setTitlesTextColorRes(theme.textPrimaryColorRes)
        screen.setSubtitlesTextColorRes(theme.textSecondaryColorRes)
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            syncWithCurrentTheme()
        }
    }

    companion object {
        const val PLAY_STORE_URL_FILESPACE = "https://play.google.com/store/apps/details?id=com.mercandalli.android.apps.files"
        const val PLAY_STORE_URL_TEAM_MERCAN = "https://play.google.com/store/apps/dev?id=8371778130997780965"
    }
}