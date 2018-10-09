package com.mercandalli.android.apps.files.settings_developer

import androidx.annotation.ColorRes

interface SettingsDeveloperContract {

    interface Screen {

        fun setSectionColor(@ColorRes colorRes: Int)

        fun setTextPrimaryColorRes(@ColorRes colorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes colorRes: Int)

        fun setDeveloperSectionLabelVisibility(visibility: Int)

        fun setDeveloperSectionVisibility(visibility: Int)

        fun setDeveloperActivationChecked(checked: Boolean)

        fun setOnlineSubLabelText(text: String)
    }

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onOnlineLoginRowClicked()

        fun onOnlinePasswordRowClicked()

        fun onActivationRowClicked()

        fun onDeveloperActivationCheckChanged(checked: Boolean)
    }
}