@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_store

import android.app.Activity
import androidx.annotation.ColorRes

interface SettingsStoreContract {

    interface Screen {

        fun setSectionColor(@ColorRes colorRes: Int)

        fun setTextPrimaryColorRes(@ColorRes colorRes: Int)

        fun setTextSecondaryColorRes(@ColorRes colorRes: Int)

        fun setPromotionGradient(dark: Boolean)
    }

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onRowClicked(activity: Activity)
    }
}
