package com.mercandalli.android.apps.files.bottom_bar

interface BottomBarContract {

    interface UserAction {

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
    }
}