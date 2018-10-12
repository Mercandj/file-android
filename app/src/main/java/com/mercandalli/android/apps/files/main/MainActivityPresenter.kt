package com.mercandalli.android.apps.files.main

import android.os.Bundle
import android.os.Environment
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileCreatorManager

class MainActivityPresenter(
        private val screen: MainActivityContract.Screen,
        private val fileCreatorManager: FileCreatorManager,
        private val fileOnlineCreatorManager: FileCreatorManager,
        private val fileCopyCutManager: FileCopyCutManager,
        private val themeManager: ThemeManager,
        private val mainActivityFileUiStorage: MainActivityFileUiStorage,
        private val rootPathLocal: String,
        private val rootPathOnline: String
) : MainActivityContract.UserAction {

    private var currentPath: String? = null
    private var selectedSection: Int = SECTION_UNDEFINED
    private val themeListener = createThemeListener()
    private val fileToPasteChangedListener = createFileToPasteChangedListener()

    override fun onCreate() {
        themeManager.registerThemeListener(themeListener)
        fileCopyCutManager.registerFileToPasteChangedListener(fileToPasteChangedListener)
        syncWithCurrentTheme()
        syncToolbarPasteIconVisibility()
    }

    override fun onDestroy() {
        themeManager.unregisterThemeListener(themeListener)
        fileCopyCutManager.unregisterFileToPasteChangedListener(fileToPasteChangedListener)
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

    override fun onOnlineSectionClicked() {
        selectOnline()
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

    override fun onToolbarUploadClicked() {
        screen.showFileUploadSelection()
    }

    override fun onToolbarFileColumnClicked() {
        selectFileColumn()
    }

    override fun onToolbarFileListClicked() {
        selectFileList()
    }

    override fun onToolbarFilePasteClicked() {
        val path = if (currentPath == null) {
            getRootPath()
        } else {
            currentPath
        }
        fileCopyCutManager.paste(path!!)
    }

    override fun onFileCreationConfirmed(fileName: String) {
        val parentPath = if (currentPath == null) {
            getRootPath()
        } else {
            currentPath
        }
        if (selectedSection == SECTION_ONLINE) {
            fileOnlineCreatorManager.create(parentPath!!, fileName)
        } else {
            fileCreatorManager.create(parentPath!!, fileName)
        }
    }

    override fun onSelectedFilePathChanged(path: String?) {
        currentPath = path
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
        currentPath = screen.getFileListCurrentPath()
        screen.showFileListView()
        screen.hideFileColumnView()
        screen.hideOnlineView()
        screen.hideNoteView()
        screen.hideSettingsView()
        screen.hideToolbarDelete()
        screen.hideToolbarShare()
        screen.showToolbarAdd()
        screen.hideToolbarUpload()
        screen.showToolbarFileColumn()
        screen.hideToolbarFileList()
        syncToolbarPasteIconVisibility()
    }

    private fun selectFileColumn() {
        mainActivityFileUiStorage.setCurrentFileUi(MainActivityFileUiStorage.SECTION_FILE_COLUMN)
        selectedSection = SECTION_FILE_COLUMN
        currentPath = screen.getFileListCurrentPath()
        screen.hideFileListView()
        screen.showFileColumnView()
        screen.hideOnlineView()
        screen.hideNoteView()
        screen.hideSettingsView()
        screen.hideToolbarDelete()
        screen.hideToolbarShare()
        screen.showToolbarAdd()
        screen.hideToolbarUpload()
        screen.hideToolbarFileColumn()
        screen.showToolbarFileList()
        syncToolbarPasteIconVisibility()
    }

    private fun selectOnline() {
        selectedSection = SECTION_ONLINE
        currentPath = screen.getFileOnlineCurrentPath()
        screen.hideFileListView()
        screen.hideFileColumnView()
        screen.showOnlineView()
        screen.hideNoteView()
        screen.hideSettingsView()
        screen.hideToolbarDelete()
        screen.hideToolbarShare()
        screen.showToolbarAdd()
        screen.showToolbarUpload()
        screen.hideToolbarFileColumn()
        screen.hideToolbarFileList()
        syncToolbarPasteIconVisibility()
    }

    private fun selectNote() {
        selectedSection = SECTION_NOTE
        screen.hideFileListView()
        screen.hideFileColumnView()
        screen.hideOnlineView()
        screen.showNoteView()
        screen.hideSettingsView()
        screen.showToolbarDelete()
        screen.showToolbarShare()
        screen.hideToolbarAdd()
        screen.hideToolbarUpload()
        screen.hideToolbarFileColumn()
        screen.hideToolbarFileList()
        syncToolbarPasteIconVisibility()
    }

    private fun selectSettings() {
        selectedSection = SECTION_SETTINGS
        screen.hideFileListView()
        screen.hideFileColumnView()
        screen.hideOnlineView()
        screen.hideNoteView()
        screen.showSettingsView()
        screen.hideToolbarDelete()
        screen.hideToolbarShare()
        screen.hideToolbarAdd()
        screen.hideToolbarUpload()
        screen.hideToolbarFileColumn()
        screen.hideToolbarFileList()
        syncToolbarPasteIconVisibility()
    }

    private fun syncWithCurrentTheme() {
        val theme = themeManager.getTheme()
        screen.setWindowBackgroundColorRes(theme.windowBackgroundColorRes)
        screen.setBottomBarBlurOverlayColorRes(theme.bottomBarBlurOverlay)
    }

    private fun syncToolbarPasteIconVisibility() {
        if (selectedSection != SECTION_FILE_LIST && selectedSection != SECTION_FILE_COLUMN) {
            screen.setPasteIconVisibility(false)
            return
        }
        val fileToPastePath = fileCopyCutManager.getFileToPastePath()
        screen.setPasteIconVisibility(fileToPastePath != null)
    }

    private fun getRootPath(): String {
        if (selectedSection == SECTION_ONLINE) {
            return rootPathOnline
        }
        return rootPathLocal
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            syncWithCurrentTheme()
        }
    }

    private fun createFileToPasteChangedListener() = object : FileCopyCutManager.FileToPasteChangedListener {
        override fun onFileToPasteChanged() {
            syncToolbarPasteIconVisibility()
        }
    }

    companion object {
        private const val SECTION_UNDEFINED = 0
        private const val SECTION_FILE_LIST = SECTION_UNDEFINED + 1
        private const val SECTION_FILE_COLUMN = SECTION_FILE_LIST + 1
        private const val SECTION_ONLINE = SECTION_FILE_COLUMN + 1
        private const val SECTION_NOTE = SECTION_ONLINE + 1
        private const val SECTION_SETTINGS = SECTION_NOTE + 1
    }
}