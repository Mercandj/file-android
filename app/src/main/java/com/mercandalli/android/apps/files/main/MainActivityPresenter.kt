package com.mercandalli.android.apps.files.main

import android.os.Bundle
import android.os.Environment
import com.mercandalli.android.apps.files.main.MainActivityFileUiStorage.Companion.SECTION_FILE_COLUMN
import com.mercandalli.android.apps.files.main.MainActivityFileUiStorage.Companion.SECTION_FILE_LIST
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.FileCreatorManager

class MainActivityPresenter(
        private val screen: MainActivityContract.Screen,
        private val fileCreatorManager: FileCreatorManager,
        private val themeManager: ThemeManager,
        private val mainActivityFileUiStorage: MainActivityFileUiStorage
) : MainActivityContract.UserAction {

    private var selectedPath: String? = null
    private var selectedSection: Int = SECTION_UNDEFINED
    private val themeListener = createThemeListener()

    init {
        themeManager.registerThemeListener(themeListener)
        syncWithCurrentTheme()
    }

    override fun onDestroy() {
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            selectFile()
            return
        }
        val section = savedInstanceState.getInt("section", SECTION_UNDEFINED)
        when (section) {
            SECTION_FILE_LIST -> selectFileList()
            SECTION_FILE_COLUMN -> selectFileColumn()
            SECTION_NOTE -> selectNote()
            SECTION_SETTINGS -> selectSettings()
            else -> selectFileColumn()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (outState == null) {
            return
        }
        outState.putInt("section", selectedSection)
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

    override fun onToolbarFileColumnClicked() {
        selectFileColumn()
    }

    override fun onToolbarFileListClicked() {
        selectFileList()
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
        val currentFileUi = mainActivityFileUiStorage.getCurrentFileUi()
        when (currentFileUi) {
            MainActivityFileUiStorage.SECTION_FILE_LIST -> selectFileList()
            MainActivityFileUiStorage.SECTION_FILE_COLUMN -> selectFileColumn()
            else -> throw IllegalStateException("Unsupported currentFileUi: $currentFileUi")
        }
    }

    private fun selectFileList() {
        mainActivityFileUiStorage.setCurrentFileUi(MainActivityFileUiStorage.SECTION_FILE_LIST)
        selectedSection = SECTION_FILE_LIST
        screen.showFileListView()
        screen.hideFileColumnView()
        screen.hideNoteView()
        screen.hideSettingsView()
        screen.hideToolbarDelete()
        screen.hideToolbarShare()
        screen.showToolbarAdd()
        screen.showToolbarFileColumn()
        screen.hideToolbarFileList()
    }

    private fun selectFileColumn() {
        mainActivityFileUiStorage.setCurrentFileUi(MainActivityFileUiStorage.SECTION_FILE_COLUMN)
        selectedSection = SECTION_FILE_COLUMN
        screen.hideFileListView()
        screen.showFileColumnView()
        screen.hideNoteView()
        screen.hideSettingsView()
        screen.hideToolbarDelete()
        screen.hideToolbarShare()
        screen.showToolbarAdd()
        screen.hideToolbarFileColumn()
        screen.showToolbarFileList()
    }

    private fun selectNote() {
        selectedSection = SECTION_NOTE
        screen.hideFileListView()
        screen.hideFileColumnView()
        screen.showNoteView()
        screen.hideSettingsView()
        screen.showToolbarDelete()
        screen.showToolbarShare()
        screen.hideToolbarAdd()
        screen.hideToolbarFileColumn()
        screen.hideToolbarFileList()
    }

    private fun selectSettings() {
        selectedSection = SECTION_SETTINGS
        screen.hideFileListView()
        screen.hideFileColumnView()
        screen.hideNoteView()
        screen.showSettingsView()
        screen.hideToolbarDelete()
        screen.hideToolbarShare()
        screen.hideToolbarAdd()
        screen.hideToolbarFileColumn()
        screen.hideToolbarFileList()
    }

    private fun syncWithCurrentTheme() {
        val theme = themeManager.theme
        screen.setWindowBackgroundColorRes(theme.windowBackgroundColorRes)
        screen.setBottomBarBlurOverlayColorRes(theme.bottomBarBlurOverlay)
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
        private const val SECTION_FILE_LIST = SECTION_UNDEFINED + 1
        private const val SECTION_FILE_COLUMN = SECTION_FILE_LIST + 1
        private const val SECTION_NOTE = SECTION_FILE_COLUMN + 1
        private const val SECTION_SETTINGS = SECTION_NOTE + 1
    }
}