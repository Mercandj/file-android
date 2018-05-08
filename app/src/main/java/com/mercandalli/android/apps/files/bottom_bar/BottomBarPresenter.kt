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
        screen.notifyListenerFileClicked()
    }

    override fun onNoteClicked() {
        selectNote()
        screen.notifyListenerNoteClicked()
    }

    override fun onSettingsClicked() {
        selectSettings()
        screen.notifyListenerSettingsClicked()
    }

    private fun selectFile() {
        screen.setFileIconColor(selectedColor)
        screen.setNoteIconColor(notSelectedColor)
        screen.setSettingsIconColor(notSelectedColor)
    }

    private fun selectNote() {
        screen.setFileIconColor(notSelectedColor)
        screen.setNoteIconColor(selectedColor)
        screen.setSettingsIconColor(notSelectedColor)
    }

    private fun selectSettings() {
        screen.setFileIconColor(notSelectedColor)
        screen.setNoteIconColor(notSelectedColor)
        screen.setSettingsIconColor(selectedColor)
    }
}