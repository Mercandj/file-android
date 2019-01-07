package com.mercandalli.android.apps.files.main

import android.os.Bundle
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileCreatorManager

internal class MainActivityPresenter(
    private val screen: MainActivityContract.Screen,
    private val fileCreatorManager: FileCreatorManager,
    private val fileOnlineCreatorManager: FileCreatorManager,
    private val fileCopyCutManager: FileCopyCutManager,
    private val fileOnlineCopyCutManager: FileCopyCutManager,
    private val themeManager: ThemeManager,
    private val mainActivityFileUiStorage: MainActivityFileUiStorage,
    private val mainActivitySectionStorage: MainActivitySectionStorage,
    private val rootPathLocal: String,
    private val rootPathOnline: String
) : MainActivityContract.UserAction {

    private var currentPath: String? = null
    private var selectedSection: Section = Section.UNDEFINED
    private val themeListener = createThemeListener()
    private val fileToPasteChangedListener = createFileToPasteChangedListener()

    override fun onCreate() {
        themeManager.registerThemeListener(themeListener)
        fileCopyCutManager.registerFileToPasteChangedListener(fileToPasteChangedListener)
        fileOnlineCopyCutManager.registerFileToPasteChangedListener(fileToPasteChangedListener)
        syncWithCurrentTheme()
        syncToolbarPasteIconVisibility()
    }

    override fun onDestroy() {
        themeManager.unregisterThemeListener(themeListener)
        fileCopyCutManager.unregisterFileToPasteChangedListener(fileToPasteChangedListener)
        fileOnlineCopyCutManager.unregisterFileToPasteChangedListener(fileToPasteChangedListener)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            when (mainActivitySectionStorage.getSection()) {
                MainActivitySectionStorage.Companion.Section.UNDEFINED -> selectFile()
                MainActivitySectionStorage.Companion.Section.FILE -> selectFile()
                MainActivitySectionStorage.Companion.Section.ONLINE -> selectFile() // Attention
                MainActivitySectionStorage.Companion.Section.NOTE -> selectNote()
                MainActivitySectionStorage.Companion.Section.SETTINGS -> selectSettings()
            }
            return
        }
        val section = savedInstanceState.getInt("section", Section.UNDEFINED.value)
        when (section) {
            Section.FILE_LIST.value -> selectFileList()
            Section.FILE_COLUMN.value -> selectFileColumn()
            Section.ONLINE.value -> selectOnline()
            Section.NOTE.value -> selectNote()
            Section.SETTINGS.value -> selectSettings()
            else -> selectFileColumn()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (outState == null) {
            return
        }
        outState.putInt("section", selectedSection.value)
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
        if (selectedSection == Section.ONLINE) {
            fileOnlineCopyCutManager.paste(path!!)
        } else {
            fileCopyCutManager.paste(path!!)
        }
    }

    override fun onFileCreationConfirmed(fileName: String) {
        val parentPath = if (currentPath == null) {
            getRootPath()
        } else {
            currentPath
        }
        if (selectedSection == Section.ONLINE) {
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
        fileCopyCutManager.cancelCopyCut()
        fileOnlineCopyCutManager.cancelCopyCut()
        mainActivityFileUiStorage.setCurrentFileUi(MainActivityFileUiStorage.SECTION_FILE_LIST)
        selectedSection = Section.FILE_LIST
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
        screen.selectBottomBarFile()
        syncToolbarPasteIconVisibility()
        mainActivitySectionStorage.putSection(MainActivitySectionStorage.Companion.Section.FILE)
    }

    private fun selectFileColumn() {
        fileCopyCutManager.cancelCopyCut()
        fileOnlineCopyCutManager.cancelCopyCut()
        mainActivityFileUiStorage.setCurrentFileUi(MainActivityFileUiStorage.SECTION_FILE_COLUMN)
        selectedSection = Section.FILE_COLUMN
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
        screen.selectBottomBarFile()
        syncToolbarPasteIconVisibility()
        mainActivitySectionStorage.putSection(MainActivitySectionStorage.Companion.Section.FILE)
    }

    private fun selectOnline() {
        fileCopyCutManager.cancelCopyCut()
        fileOnlineCopyCutManager.cancelCopyCut()
        selectedSection = Section.ONLINE
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
        screen.selectBottomBarOnline()
        syncToolbarPasteIconVisibility()
        mainActivitySectionStorage.putSection(MainActivitySectionStorage.Companion.Section.ONLINE)
    }

    private fun selectNote() {
        selectedSection = Section.NOTE
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
        screen.selectBottomBarNote()
        syncToolbarPasteIconVisibility()
        mainActivitySectionStorage.putSection(MainActivitySectionStorage.Companion.Section.NOTE)
    }

    private fun selectSettings() {
        selectedSection = Section.SETTINGS
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
        screen.selectBottomBarSettings()
        syncToolbarPasteIconVisibility()
        mainActivitySectionStorage.putSection(MainActivitySectionStorage.Companion.Section.SETTINGS)
    }

    private fun syncWithCurrentTheme() {
        val theme = themeManager.getTheme()
        screen.setWindowBackgroundColorRes(theme.windowBackgroundColorRes)
        screen.setBottomBarBlurOverlayColorRes(theme.bottomBarBlurOverlay)
    }

    private fun syncToolbarPasteIconVisibility() {
        if (selectedSection != Section.FILE_LIST &&
            selectedSection != Section.FILE_COLUMN &&
            selectedSection != Section.ONLINE
        ) {
            screen.setPasteIconVisibility(false)
            return
        }
        val fileToPastePath = if (selectedSection != Section.ONLINE) {
            fileCopyCutManager.getFileToPastePath()
        } else {
            fileOnlineCopyCutManager.getFileToPastePath()
        }
        screen.setPasteIconVisibility(fileToPastePath != null)
    }

    private fun getRootPath(): String {
        if (selectedSection == Section.ONLINE) {
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

    enum class Section(val value: Int) {
        UNDEFINED(1),
        FILE_LIST(2),
        FILE_COLUMN(3),
        ONLINE(4),
        NOTE(5),
        SETTINGS(6)
    }
}
