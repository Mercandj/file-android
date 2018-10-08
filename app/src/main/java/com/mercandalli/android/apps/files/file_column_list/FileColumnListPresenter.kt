package com.mercandalli.android.apps.files.file_column_list

import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.FileSortManager

class FileColumnListPresenter(
        private val screen: FileColumnListContract.Screen,
        private var fileManager: FileManager,
        private val fileSortManager: FileSortManager,
        private val themeManager: ThemeManager,
        private var currentPath: String
) : FileColumnListContract.UserAction {

    private val fileChildrenResultListener = createFileChildrenResultListener()
    private val themeListener = createThemeListener()

    override fun onAttached() {
        fileManager.registerFileChildrenResultListener(fileChildrenResultListener)
        syncFileChildren()
        themeManager.registerThemeListener(themeListener)
        syncWithCurrentTheme()
    }

    override fun onDetached() {
        fileManager.unregisterFileChildrenResultListener(fileChildrenResultListener)
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onResume() {
        syncFileChildren()
    }

    override fun onRefresh() {
        val fileChildrenResult = fileManager.loadFileChildren(currentPath, true)
        syncFileChildren(fileChildrenResult)
    }

    override fun onPathChanged(path: String) {
        currentPath = path
        syncFileChildren()
    }

    override fun onPathSelected(path: String?) {
        screen.selectPath(path)
    }

    override fun onSetFileManager(fileManager: FileManager) {
        this.fileManager.unregisterFileChildrenResultListener(fileChildrenResultListener)
        this.fileManager = fileManager
        fileManager.registerFileChildrenResultListener(fileChildrenResultListener)
    }

    private fun syncFileChildren() {
        var fileChildrenResult = fileManager.getFileChildren(currentPath)
        if (fileChildrenResult.status == FileChildrenResult.Status.UNLOADED ||
                fileChildrenResult.status == FileChildrenResult.Status.ERROR_NOT_FOLDER) {
            fileChildrenResult = fileManager.loadFileChildren(currentPath)
        }
        syncFileChildren(fileChildrenResult)
    }

    private fun syncFileChildren(fileChildrenResult: FileChildrenResult) {
        when (fileChildrenResult.status) {
            FileChildrenResult.Status.UNLOADED, FileChildrenResult.Status.ERROR_NOT_FOLDER -> {
                screen.hideEmptyView()
                screen.showErrorMessage()
                screen.hideFiles()
                screen.hideLoader()
            }
            FileChildrenResult.Status.LOADING -> {
                screen.hideEmptyView()
                screen.hideErrorMessage()
                screen.showLoader()
            }
            FileChildrenResult.Status.LOADED_SUCCEEDED -> {
                val files = fileChildrenResult.getFiles()
                screen.hideErrorMessage()
                screen.hideLoader()
                if (files.isEmpty()) {
                    screen.showEmptyView()
                    screen.hideFiles()
                } else {
                    val filesSorted = fileSortManager.sort(files)
                    screen.hideEmptyView()
                    screen.showFiles(filesSorted)
                }
            }
        }
    }

    private fun syncWithCurrentTheme() {
        val theme = themeManager.getTheme()
        screen.setEmptyTextColorRes(theme.textPrimaryColorRes)
        screen.setErrorTextColorRes(theme.textSecondaryColorRes)
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            syncWithCurrentTheme()
        }
    }

    private fun createFileChildrenResultListener() = object : FileManager.FileChildrenResultListener {
        override fun onFileChildrenResultChanged(path: String) {
            if (currentPath != path) {
                return
            }
            val fileChildren = fileManager.getFileChildren(currentPath)
            syncFileChildren(fileChildren)
        }
    }
}