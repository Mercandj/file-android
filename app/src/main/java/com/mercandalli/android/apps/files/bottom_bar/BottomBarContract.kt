@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.bottom_bar

import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

interface BottomBarContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onSaveInstanceState(saveState: Bundle)

        fun onRestoreInstanceState(state: Bundle)

        fun onFileClicked()

        fun onOnlineClicked()

        fun onNoteClicked()

        fun onSettingsClicked()

        fun onSelectFile()

        fun onSelectOnline()

        fun onSelectNote()

        fun onSelectSettings()
    }

    interface Screen {

        fun notifyListenerFileClicked()

        fun notifyListenerOnlineClicked()

        fun notifyListenerNoteClicked()

        fun notifyListenerSettingsClicked()

        fun showOnlineSection()

        fun hideOnlineSection()

        fun setFileIconColorRes(@ColorRes colorRes: Int)

        fun setOnlineIconColorRes(@ColorRes colorRes: Int)

        fun setNoteIconColorRes(@ColorRes colorRes: Int)

        fun setSettingsIconColorRes(@ColorRes colorRes: Int)

        fun setSectionFileTextColorRes(@ColorRes colorRes: Int)

        fun setSectionOnlineTextColorRes(@ColorRes colorRes: Int)

        fun setSectionNoteTextColorRes(@ColorRes colorRes: Int)

        fun setSectionSettingsTextColorRes(@ColorRes colorRes: Int)
    }
}
