package com.mercandalli.android.apps.files.main

class MainActivityPresenter(
        private val screen: MainActivityContract.Screen
) : MainActivityContract.UserAction {

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

    private fun selectFile() {
        screen.showFileView()
        screen.hideNoteView()
        screen.hideSettingsView()
    }

    private fun selectNote() {
        screen.hideFileView()
        screen.showNoteView()
        screen.hideSettingsView()
    }

    private fun selectSettings() {
        screen.hideFileView()
        screen.hideNoteView()
        screen.showSettingsView()
    }
}