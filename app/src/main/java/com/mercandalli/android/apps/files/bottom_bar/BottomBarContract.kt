package com.mercandalli.android.apps.files.bottom_bar

import android.os.Bundle

interface BottomBarContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()

        fun onSaveInstanceState(saveState: Bundle)

        fun onRestoreInstanceState(state: Bundle)

        fun onFileClicked()

        fun onNoteClicked()

        fun onSettingsClicked()
    }

    interface Screen {

        fun notifyListenerFileClicked()

        fun notifyListenerNoteClicked()

        fun notifyListenerSettingsClicked()

        fun setFileIconColor(color: Int)

        fun setNoteIconColor(color: Int)

        fun setSettingsIconColor(color: Int)

        fun setSectionFileTextColorRes(textColorRes: Int)

        fun setSectionNoteTextColorRes(textColorRes: Int)

        fun setSectionSettingsTextColorRes(textColorRes: Int)
    }
}