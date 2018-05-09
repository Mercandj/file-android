package com.mercandalli.android.apps.files.main

interface MainActivityContract {

    interface UserAction {

        fun onFileSectionClicked()

        fun onNoteSectionClicked()

        fun onSettingsSectionClicked()
    }

    interface Screen {

        fun showFileView()

        fun hideFileView()

        fun showNoteView()

        fun hideNoteView()

        fun showSettingsView()

        fun hideSettingsView()
    }
}