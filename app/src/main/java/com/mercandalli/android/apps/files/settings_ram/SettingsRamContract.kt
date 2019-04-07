@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_ram

import androidx.annotation.ColorRes

interface SettingsRamContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onStorageLocalRowClicked()
    }

    interface Screen {

        fun setTextPrimaryColorRes(@ColorRes colorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes colorRes: Int)

        fun setSectionColor(@ColorRes colorRes: Int)

        fun setLocalSubLabelText(text: String)

        fun setLocalBusy(text: String)

        fun setLocalTotal(text: String)

        fun setProgress(percent: Float)
    }
}
