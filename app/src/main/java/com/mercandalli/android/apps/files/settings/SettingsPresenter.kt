package com.mercandalli.android.apps.files.settings

import com.mercandalli.android.apps.files.version.VersionManager

class SettingsPresenter(
        private val screen: SettingsContract.Screen,
        versionManager: VersionManager
) : SettingsContract.UserAction {

    init {
        val versionName = versionManager.getVersionName()
        screen.showVersionName(versionName)
    }

    override fun onRateClicked() {
        screen.openUrl(PLAY_STORE_URL_FILESPACE)
    }

    override fun onTeamAppsClicked() {
        screen.openUrl(PLAY_STORE_URL_TEAM_MERCAN)
    }

    companion object {
        const val PLAY_STORE_URL_FILESPACE = "https://play.google.com/store/apps/details?id=com.mercandalli.android.apps.files"
        const val PLAY_STORE_URL_TEAM_MERCAN = "https://play.google.com/store/apps/dev?id=8371778130997780965"
    }
}