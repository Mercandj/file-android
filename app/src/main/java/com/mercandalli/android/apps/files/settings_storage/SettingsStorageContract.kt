@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_storage

import androidx.annotation.ColorRes

interface SettingsStorageContract {

    interface Screen {

        fun setTextPrimaryColorRes(@ColorRes colorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes colorRes: Int)

        fun setSectionColor(@ColorRes colorRes: Int)

        fun setLocalSubLabelText(text: String)
    }

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onStorageLocalRowClicked()
    }
}
