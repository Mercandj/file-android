@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_android_q

import androidx.annotation.ColorRes

interface SettingsAndroidQContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onRowClicked()
    }

    interface Screen {

        fun setTextPrimaryColorRes(@ColorRes colorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes colorRes: Int)

        fun setSectionColor(@ColorRes colorRes: Int)

        fun startAndroidQActivity()
    }
}
