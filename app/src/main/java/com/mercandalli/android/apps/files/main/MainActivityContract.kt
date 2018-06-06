package com.mercandalli.android.apps.files.main

import android.os.Bundle

interface MainActivityContract {

    interface UserAction {

        fun onDestroy()

        fun onRestoreInstanceState(savedInstanceState: Bundle?)

        fun onSaveInstanceState(outState: Bundle?)

        fun onFileSectionClicked()

        fun onNoteSectionClicked()

        fun onSettingsSectionClicked()

        fun onToolbarDeleteClicked()

        fun onToolbarShareClicked()

        fun onToolbarAddClicked()

        fun onFileCreationConfirmed(fileName: String)

        fun onSelectedFilePathChanged(path: String?)
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

        fun showToolbarAdd()

        fun hideToolbarAdd()

        fun deleteNote()

        fun shareNote()

        fun showFileCreationSelection()

        fun setWindowBackgroundColorRes(windowBackgroundColorRes: Int)
    }
}