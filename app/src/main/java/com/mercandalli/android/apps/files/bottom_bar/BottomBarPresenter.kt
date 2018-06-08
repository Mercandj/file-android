package com.mercandalli.android.apps.files.bottom_bar

import android.os.Bundle
import com.mercandalli.android.apps.files.theme.ThemeManager

class BottomBarPresenter(
        private val screen: BottomBarContract.Screen,
        private val themeManager: ThemeManager,
        private val selectedColor: Int,
        private val notSelectedColor: Int
) : BottomBarContract.UserAction {

    private var selectedSection: Int = SECTION_UNDEFINED
    private val themeListener = createThemeListener()

    init {
        selectFile()
    }

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        syncWithCurrentTheme()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onSaveInstanceState(saveState: Bundle) {
        saveState.putInt("BUNDLE_KEY_SECTION", selectedSection)
    }

    override fun onRestoreInstanceState(state: Bundle) {
        val section = state.getInt("BUNDLE_KEY_SECTION")
        when (section) {
            SECTION_FILE -> selectFile()
            SECTION_NOTE -> selectNote()
            SECTION_SETTINGS -> selectSettings()
            else -> selectFile()
        }
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
        selectedSection = SECTION_FILE
        screen.setFileIconColor(selectedColor)
        screen.setNoteIconColor(notSelectedColor)
        screen.setSettingsIconColor(notSelectedColor)
    }

    private fun selectNote() {
        selectedSection = SECTION_NOTE
        screen.setFileIconColor(notSelectedColor)
        screen.setNoteIconColor(selectedColor)
        screen.setSettingsIconColor(notSelectedColor)
    }

    private fun selectSettings() {
        selectedSection = SECTION_SETTINGS
        screen.setFileIconColor(notSelectedColor)
        screen.setNoteIconColor(notSelectedColor)
        screen.setSettingsIconColor(selectedColor)
    }

    private fun syncWithCurrentTheme() {
        val theme = themeManager.theme
        screen.setSectionFileTextColorRes(theme.textPrimaryColorRes)
        screen.setSectionNoteTextColorRes(theme.textPrimaryColorRes)
        screen.setSectionSettingsTextColorRes(theme.textPrimaryColorRes)
    }

    private fun createThemeListener(): ThemeManager.OnCurrentThemeChangeListener {
        return object : ThemeManager.OnCurrentThemeChangeListener {
            override fun onCurrentThemeChanged() {
                syncWithCurrentTheme()
            }
        }
    }

    companion object {
        private const val SECTION_UNDEFINED = 0
        private const val SECTION_FILE = 1
        private const val SECTION_NOTE = 2
        private const val SECTION_SETTINGS = 3
    }
}