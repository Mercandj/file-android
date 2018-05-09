package com.mercandalli.android.apps.files.main

import android.os.Environment
import com.mercandalli.sdk.files.api.FileCreatorManager

class MainActivityPresenter(
        private val screen: MainActivityContract.Screen,
        private val fileCreatorManager: FileCreatorManager
) : MainActivityContract.UserAction {

    private var selectedPath: String? = null

    init {
        selectFile()
    }

    override fun onFileSectionClicked() {
        selectFile()
    }

    override fun onNoteSectionClicked() {
        selectNote()
    }

    override fun onSettingsSectionClicked() {
        selectSettings()
    }

    override fun onToolbarDeleteClicked() {
        screen.deleteNote()
    }

    override fun onToolbarShareClicked() {
        screen.shareNote()
    }

    override fun onToolbarAddClicked() {
        screen.showFileCreationSelection()
    }

    override fun onFileCreationConfirmed(fileName: String) {
        val path = if (selectedPath == null) Environment.getExternalStorageDirectory().absolutePath
        else selectedPath
        fileCreatorManager.create(path!!, fileName)
    }

    override fun onSelectedFilePathChanged(path: String?) {
        selectedPath = path
    }

    private fun selectFile() {
        screen.showFileView()
        screen.hideNoteView()
        screen.hideSettingsView()
        screen.hideToolbarDelete()
        screen.hideToolbarShare()
        screen.showToolbarAdd()
    }

    private fun selectNote() {
        screen.hideFileView()
        screen.showNoteView()
        screen.hideSettingsView()
        screen.showToolbarDelete()
        screen.showToolbarShare()
        screen.hideToolbarAdd()
    }

    private fun selectSettings() {
        screen.hideFileView()
        screen.hideNoteView()
        screen.showSettingsView()
        screen.hideToolbarDelete()
        screen.hideToolbarShare()
        screen.hideToolbarAdd()
    }
}