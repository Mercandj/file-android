@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_about

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface SettingsAboutContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onRateClicked()

        fun onTeamAppsClicked()

        fun onVersionClicked()
    }

    interface Screen {

        fun openUrl(url: String)

        fun showVersionName(versionName: String)

        fun setCardBackgroundColorRes(@ColorRes colorRes: Int)

        fun setTitlesTextColorRes(@ColorRes colorRes: Int)

        fun setSubtitlesTextColorRes(@ColorRes colorRes: Int)

        fun showSnackbar(@StringRes messageStringRes: Int, duration: Int)
    }
}
