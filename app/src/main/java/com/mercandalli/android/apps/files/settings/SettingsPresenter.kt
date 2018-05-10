package com.mercandalli.android.apps.files.settings

import com.mercandalli.android.apps.files.version.VersionManager

class SettingsPresenter(
        private val screen: SettingsContract.Screen,
        private val versionManager: VersionManager
) : SettingsContract.UserAction {

    init {
        val versionName = versionManager.getVersionName()
        screen.showVersionName(versionName)
    }

    override fun onRateClicked() {
        screen.openUrl("https://play.google.com/store/apps/details?id=com.mercandalli.android.apps.files")
    }

    override fun onTeamAppsClicked() {
        screen.openUrl("https://play.google.com/store/apps/dev?id=8371778130997780965")
    }
}