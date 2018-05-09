package com.mercandalli.android.apps.files.settings

interface SettingsContract {

    interface UserAction {

        fun onRateClicked()

        fun onTeamAppsClicked()
    }

    interface Screen {
        fun openUrl(url: String)
    }
}