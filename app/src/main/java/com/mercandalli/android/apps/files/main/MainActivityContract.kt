package com.mercandalli.android.apps.files.main

interface MainActivityContract {

    interface UserAction {

        fun onFileSectionClicked()

        fun onNoteSectionClicked()

        fun onSettingsSectionClicked()

        fun onToolbarDeleteClicked()

        fun onToolbarShareClicked()
    }

    interface Screen {

        fun showFileView()

        fun hideFileView()

        fun showNoteView()

        fun hideNoteView()

        fun showSettingsView()

        fun hideSettingsView()

        fun showToolbarDelete()

        fun hideToolbarDelete()

        fun showToolbarShare()

        fun hideToolbarShare()

        fun deleteNote()

        fun shareNote()
    }
}