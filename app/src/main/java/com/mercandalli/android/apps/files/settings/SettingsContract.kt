package com.mercandalli.android.apps.files.settings

interface SettingsContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onRateClicked()

        fun onTeamAppsClicked()

        fun onThemeRowClicked(checked: Boolean)

        fun onThemeCheckboxCheckedChange(checked: Boolean)
    }

    interface Screen {

        fun openUrl(url: String)

        fun showVersionName(versionName: String)

        fun setThemeCheckboxChecked(checked: Boolean)
    }
}