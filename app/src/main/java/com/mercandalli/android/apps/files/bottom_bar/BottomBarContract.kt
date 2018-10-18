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
    }

    interface Screen {

        fun notifyListenerFileClicked()

        fun notifyListenerOnlineClicked()

        fun notifyListenerNoteClicked()

        fun notifyListenerSettingsClicked()

        fun showOnlineSection()

        fun hideOnlineSection()

        fun setFileIconColor(@ColorInt color: Int)

        fun setOnlineIconColor(@ColorInt color: Int)

        fun setNoteIconColor(@ColorInt color: Int)

        fun setSettingsIconColor(@ColorInt color: Int)

        fun setSectionFileTextColorRes(@ColorRes colorRes: Int)

        fun setSectionOnlineTextColorRes(@ColorRes colorRes: Int)

        fun setSectionNoteTextColorRes(@ColorRes colorRes: Int)

        fun setSectionSettingsTextColorRes(@ColorRes colorRes: Int)
    }
}
