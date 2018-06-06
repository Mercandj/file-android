package com.mercandalli.android.apps.files.settings

import com.mercandalli.android.apps.files.theme.DarkTheme
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.version.VersionManager

class SettingsPresenter(
        private val screen: SettingsContract.Screen,
        versionManager: VersionManager,
        private val themeManager: ThemeManager
) : SettingsContract.UserAction {

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
        val theme = themeManager.theme
        screen.setThemeCheckboxChecked(theme is DarkTheme)
    }

    private fun createThemeListener() = object : ThemeManager.OnCurrentThemeChangeListener {
        override fun onCurrentThemeChanged() {
            syncWithCurrentTheme()
        }
    }

    companion object {
        const val PLAY_STORE_URL_FILESPACE = "https://play.google.com/store/apps/details?id=com.mercandalli.android.apps.files"
        const val PLAY_STORE_URL_TEAM_MERCAN = "https://play.google.com/store/apps/dev?id=8371778130997780965"
    }
}