package com.mercandalli.android.apps.files.bottom_bar

class BottomBarPresenter(
        private val screen: BottomBarContract.Screen,
        private val selectedColor: Int,
        private val notSelectedColor: Int
) : BottomBarContract.UserAction {

    init {
        selectFile()
    }

    override fun onFileClicked() {
        selectFile()
    }

    override fun onNoteClicked() {
        selectNote()
    }

    override fun onSettingsClicked() {
        selectSettings()
    }

    private fun selectFile() {
        screen.setFileIconColor(selectedColor)
        screen.setNoteIconColor(notSelectedColor)
        screen.setSettingsIconColor(notSelectedColor)
        screen.notifyListenerFileClicked()
    }

    private fun selectNote() {
        screen.setFileIconColor(notSelectedColor)
        screen.setNoteIconColor(selectedColor)
        screen.setSettingsIconColor(notSelectedColor)
        screen.notifyListenerNoteClicked()
    }

    private fun selectSettings() {
        screen.setFileIconColor(notSelectedColor)
        screen.setNoteIconColor(notSelectedColor)
        screen.setSettingsIconColor(selectedColor)
        screen.notifyListenerSettingsClicked()
    }
}