@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.bottom_bar

import android.os.Bundle
import androidx.annotation.ColorRes
import com.mercandalli.android.apps.files.developer.DeveloperManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.online.FileOnlineLoginManager
import java.lang.IllegalStateException

class BottomBarPresenter(
    private val screen: BottomBarContract.Screen,
    private val themeManager: ThemeManager,
    private val developerManager: DeveloperManager,
    private val fileOnlineLoginManager: FileOnlineLoginManager,
    @ColorRes private val selectedColorRes: Int,
    @ColorRes private val notSelectedColorRes: Int
) : BottomBarContract.UserAction {

    private var selectedSection = Section.UNDEFINED
    private val themeListener = createThemeListener()
    private val developerModeListener = createDeveloperModeListener()
    private val fileOnlineLoginListener = createFileOnlineLoginListener()

    init {
        selectFile()
    }

    override fun onAttached() {
        themeManager.registerThemeListener(themeListener)
        developerManager.registerDeveloperModeListener(developerModeListener)
        fileOnlineLoginManager.registerLoginListener(fileOnlineLoginListener)
        syncWithCurrentTheme()
        syncWithDeveloperMode()
    }

    override fun onDetached() {
        themeManager.unregisterThemeListener(themeListener)
        developerManager.unregisterDeveloperModeListener(developerModeListener)
        fileOnlineLoginManager.unregisterLoginListener(fileOnlineLoginListener)
    }

    override fun onSaveInstanceState(saveState: Bundle) {
        saveState.putInt("BUNDLE_KEY_SECTION", selectedSection.sectionId)
    }

    override fun onRestoreInstanceState(state: Bundle) {
        val sectionId = state.getInt("BUNDLE_KEY_SECTION")
        val section = getSection(sectionId)
        when (section) {
            Section.FILE -> selectFile()
            Section.NOTE -> selectNote()
            Section.SETTINGS -> selectSettings()
            else -> selectFile()
        }
    }

    override fun onFileClicked() {
        selectFile()
        screen.notifyListenerFileClicked()
    }

    override fun onOnlineClicked() {
        selectOnline()
        screen.notifyListenerOnlineClicked()
    }

    override fun onNoteClicked() {
        selectNote()
        screen.notifyListenerNoteClicked()
    }

    override fun onSettingsClicked() {
        selectSettings()
        screen.notifyListenerSettingsClicked()
    }

    override fun onSelectFile() {
        selectFile()
    }

    override fun onSelectOnline() {
        selectOnline()
    }

    override fun onSelectNote() {
        selectNote()
    }

    override fun onSelectSettings() {
        selectSettings()
    }

    private fun selectFile() {
        selectedSection = Section.FILE
        syncTexts()
        syncIconsWithSelectedSection()
    }

    private fun selectOnline() {
        selectedSection = Section.ONLINE
        syncTexts()
        syncIconsWithSelectedSection()
    }

    private fun selectNote() {
        selectedSection = Section.NOTE
        syncTexts()
        syncIconsWithSelectedSection()
    }

    private fun selectSettings() {
        selectedSection = Section.SETTINGS
        syncTexts()
        syncIconsWithSelectedSection()
    }

    private fun syncWithCurrentTheme() {
        syncTexts()
    }

    private fun syncTexts() {
        val theme = themeManager.getTheme()
        when (selectedSection) {
            Section.UNDEFINED -> throw IllegalStateException("Section should be affected")
            Section.FILE -> {
                screen.setSectionFileTextColorRes(selectedColorRes)
                screen.setSectionOnlineTextColorRes(theme.textPrimaryColorRes)
                screen.setSectionNoteTextColorRes(theme.textPrimaryColorRes)
                screen.setSectionSettingsTextColorRes(theme.textPrimaryColorRes)
            }
            Section.ONLINE -> {
                screen.setSectionFileTextColorRes(theme.textPrimaryColorRes)
                screen.setSectionOnlineTextColorRes(selectedColorRes)
                screen.setSectionNoteTextColorRes(theme.textPrimaryColorRes)
                screen.setSectionSettingsTextColorRes(theme.textPrimaryColorRes)
            }
            Section.NOTE -> {
                screen.setSectionFileTextColorRes(theme.textPrimaryColorRes)
                screen.setSectionOnlineTextColorRes(theme.textPrimaryColorRes)
                screen.setSectionNoteTextColorRes(selectedColorRes)
                screen.setSectionSettingsTextColorRes(theme.textPrimaryColorRes)
            }
            Section.SETTINGS -> {
                screen.setSectionFileTextColorRes(theme.textPrimaryColorRes)
                screen.setSectionOnlineTextColorRes(theme.textPrimaryColorRes)
                screen.setSectionNoteTextColorRes(theme.textPrimaryColorRes)
                screen.setSectionSettingsTextColorRes(selectedColorRes)
            }
        }
    }

    private fun syncIconsWithSelectedSection() {
        when (selectedSection) {
            Section.UNDEFINED -> throw IllegalStateException("Section should be affected")
            Section.FILE -> {
                screen.setFileIconColorRes(selectedColorRes)
                screen.setOnlineIconColorRes(notSelectedColorRes)
                screen.setNoteIconColorRes(notSelectedColorRes)
                screen.setSettingsIconColorRes(notSelectedColorRes)
            }
            Section.ONLINE -> {
                screen.setFileIconColorRes(notSelectedColorRes)
                screen.setOnlineIconColorRes(selectedColorRes)
                screen.setNoteIconColorRes(notSelectedColorRes)
                screen.setSettingsIconColorRes(notSelectedColorRes)
            }
            Section.NOTE -> {
                screen.setFileIconColorRes(notSelectedColorRes)
                screen.setOnlineIconColorRes(notSelectedColorRes)
                screen.setNoteIconColorRes(selectedColorRes)
                screen.setSettingsIconColorRes(notSelectedColorRes)
            }
            Section.SETTINGS -> {
                screen.setFileIconColorRes(notSelectedColorRes)
                screen.setOnlineIconColorRes(notSelectedColorRes)
                screen.setNoteIconColorRes(notSelectedColorRes)
                screen.setSettingsIconColorRes(selectedColorRes)
            }
        }
    }

    private fun syncWithDeveloperMode(
        developerMode: Boolean = developerManager.isDeveloperMode(),
        isLogged: Boolean = fileOnlineLoginManager.isLogged()
    ) {
        if (developerMode && isLogged) {
            screen.showOnlineSection()
        } else {
            screen.hideOnlineSection()
        }
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            syncWithCurrentTheme()
        }
    }

    private fun createDeveloperModeListener() = object : DeveloperManager.DeveloperModeListener {
        override fun onDeveloperModeChanged() {
            syncWithDeveloperMode()
        }
    }

    private fun createFileOnlineLoginListener() = object : FileOnlineLoginManager.LoginListener {
        override fun onOnlineLogChanged() {
            syncWithDeveloperMode()
        }
    }

    private enum class Section(val sectionId: Int) {
        UNDEFINED(0),
        FILE(1),
        ONLINE(2),
        NOTE(3),
        SETTINGS(4)
    }

    companion object {

        private fun getSection(sectionId: Int) = when (sectionId) {
            0 -> Section.UNDEFINED
            1 -> Section.FILE
            2 -> Section.ONLINE
            3 -> Section.NOTE
            4 -> Section.SETTINGS
            else -> throw IllegalStateException("Wrong sectionId: $sectionId")
        }
    }
}
