package com.mercandalli.android.apps.files.file_list

import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileManager
import com.mercandalli.sdk.files.api.FileOpenManager
import com.mercandalli.sdk.files.api.FileSortManager

class FileListPresenter(
        private val screen: FileListContract.Screen,
        private var fileManager: FileManager,
        private var fileOpenManager: FileOpenManager,
        private val fileSortManager: FileSortManager,
        private val themeManager: ThemeManager,
        private var rootPath: String
) : FileListContract.UserAction {

    private var currentPath = rootPath
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

    override fun onRefresh() {
        val fileChildrenResult = fileManager.loadFileChildren(currentPath, true)
        syncFileChildren(fileChildrenResult)
    }

    override fun onFileClicked(file: File) {
        if (file.directory) {
            currentPath = file.path
            screen.notifyListenerCurrentPathChanged(currentPath)
            syncFileChildren()
            return
        }
        fileOpenManager.open(file.path)
    }

    override fun onFabUpArrowClicked() {
        if (currentPath == rootPath) {
            return
        }
        val ioFile = java.io.File(currentPath)
        val parent = ioFile.parent ?: return
        currentPath = parent
        screen.notifyListenerCurrentPathChanged(currentPath)
        syncFileChildren()
    }

    override fun onSetFileManagers(
            fileManager: FileManager,
            fileOpenManager: FileOpenManager,
            rootPath: String
    ) {
        this.fileManager.unregisterFileChildrenResultListener(fileChildrenResultListener)
        this.fileManager = fileManager
        this.fileOpenManager = fileOpenManager
        this.rootPath = rootPath
        this.currentPath = rootPath
        fileManager.registerFileChildrenResultListener(fileChildrenResultListener)
    }

    override fun getCurrentPath() = currentPath

    private fun syncFileChildren() {
        var fileChildrenResult = fileManager.getFileChildren(currentPath)
        if (fileChildrenResult.status == FileChildrenResult.Status.UNLOADED ||
                fileChildrenResult.status == FileChildrenResult.Status.ERROR_NOT_FOLDER) {
            fileChildrenResult = fileManager.loadFileChildren(currentPath)
        }
        syncFileChildren(fileChildrenResult)
        if (rootPath == currentPath) {
            screen.hideFabUpArrow()
            return
        }
        val ioFile = java.io.File(currentPath)
        val parent = ioFile.parent
        if (parent == null) {
            screen.hideFabUpArrow()
            return
        }
        screen.showFabUpArrow()
    }

    private fun syncFileChildren(fileChildrenResult: FileChildrenResult) {
        when (fileChildrenResult.status) {
            FileChildrenResult.Status.UNLOADED,
            FileChildrenResult.Status.ERROR_NOT_FOLDER,
            FileChildrenResult.Status.ERROR_NETWORK -> {
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