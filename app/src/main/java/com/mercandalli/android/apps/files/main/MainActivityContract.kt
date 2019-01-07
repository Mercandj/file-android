package com.mercandalli.android.apps.files.main

import android.os.Bundle
import androidx.annotation.ColorRes

interface MainActivityContract {

    interface UserAction {

        fun onCreate()

        fun onDestroy()

        fun onRestoreInstanceState(savedInstanceState: Bundle?)

        fun onSaveInstanceState(outState: Bundle?)

        fun onFileSectionClicked()

        fun onOnlineSectionClicked()

        fun onNoteSectionClicked()

        fun onSettingsSectionClicked()

        fun onToolbarDeleteClicked()

        fun onToolbarShareClicked()

        fun onToolbarAddClicked()

        fun onToolbarUploadClicked()

        fun onToolbarFileColumnClicked()

        fun onToolbarFileListClicked()

        fun onToolbarFilePasteClicked()

        fun onFileCreationConfirmed(fileName: String)

        fun onSelectedFilePathChanged(path: String?)
    }

    interface Screen {

        fun showFileListView()

        fun hideFileListView()

        fun showFileColumnView()

        fun hideFileColumnView()

        fun showOnlineView()

        fun hideOnlineView()

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

        fun showToolbarUpload()

        fun hideToolbarUpload()

        fun showToolbarFileColumn()

        fun hideToolbarFileColumn()

        fun showToolbarFileList()

        fun hideToolbarFileList()

        fun showToolbarFilePaste()

        fun hideToolbarFilePaste()

        fun selectBottomBarFile()

        fun selectBottomBarOnline()

        fun selectBottomBarNote()

        fun selectBottomBarSettings()

        fun deleteNote()

        fun shareNote()

        fun showFileCreationSelection()

        fun showFileUploadSelection()

        fun setWindowBackgroundColorRes(@ColorRes colorRes: Int)

        fun setBottomBarBlurOverlayColorRes(@ColorRes colorRes: Int)

        fun setPasteIconVisibility(visible: Boolean)

        fun getFileListCurrentPath(): String

        fun getFileOnlineCurrentPath(): String
    }
}
