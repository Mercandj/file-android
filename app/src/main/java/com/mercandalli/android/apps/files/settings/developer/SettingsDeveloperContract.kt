package com.mercandalli.android.apps.files.settings.developer

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface SettingsDeveloperContract {

    interface Screen {

        fun setSectionColor(@ColorRes colorRes: Int)

        fun setTextPrimaryColorRes(@ColorRes colorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes colorRes: Int)

        fun setDeveloperSectionLabelVisibility(visibility: Int)

        fun setDeveloperSectionVisibility(visibility: Int)

        fun setDeveloperActivationChecked(checked: Boolean)

        fun setDeveloperRemoteCountrySubLabelText(text: String)
    }

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onVideoStartedCountClicked()

        fun onActivationRowClicked()

        fun onDeveloperActivationCheckChanged(checked: Boolean)
    }
}