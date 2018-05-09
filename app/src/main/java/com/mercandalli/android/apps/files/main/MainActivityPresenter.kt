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

    override fun onToolbarDeleteClicked() {
        screen.deleteNote()
    }

    override fun onToolbarShareClicked() {
        screen.shareNote()
    }

    private fun selectFile() {
        screen.showFileView()
        screen.hideNoteView()
        screen.hideSettingsView()
        screen.hideToolbarDelete()
        screen.hideToolbarShare()
    }

    private fun selectNote() {
        screen.hideFileView()
        screen.showNoteView()
        screen.hideSettingsView()
        screen.showToolbarDelete()
        screen.showToolbarShare()
    }

    private fun selectSettings() {
        screen.hideFileView()
        screen.hideNoteView()
        screen.showSettingsView()
        screen.hideToolbarDelete()
        screen.hideToolbarShare()
    }
}