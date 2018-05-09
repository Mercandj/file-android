package com.mercandalli.android.apps.files.settings

class SettingsPresenter(
        private val screen: SettingsContract.Screen
) : SettingsContract.UserAction {

    override fun onRateClicked() {
        screen.openUrl("https://play.google.com/store/apps/details?id=com.mercandalli.android.apps.files")
    }

    override fun onTeamAppsClicked() {
        screen.openUrl("https://play.google.com/store/apps/dev?id=8371778130997780965")
    }
}