package com.mercandalli.android.apps.files.settings.about

import androidx.annotation.ColorRes

interface SettingsAboutContract {

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

        fun setCardBackgroundColorRes(@ColorRes colorRes: Int)

        fun setTitlesTextColorRes(@ColorRes colorRes: Int)

        fun setSubtitlesTextColorRes(@ColorRes colorRes: Int)
    }
}