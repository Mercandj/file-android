@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_list

import com.mercandalli.android.apps.files.file_provider.FileProvider
import com.mercandalli.android.apps.files.file_provider_root.FileProviderRootManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileOpenManager
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FilePathManager
import com.mercandalli.sdk.files.api.FileSortManager
import com.mercandalli.sdk.files.api.FileChildrenResult

class FileListPresenter(
    private val screen: FileListContract.Screen,
    private var fileChildrenManager: FileChildrenManager,
    private var fileOpenManager: FileOpenManager,
    private val filePathManager: FilePathManager,
    fileProviderRootManager: FileProviderRootManager,
    private val fileSortManager: FileSortManager,
    private val themeManager: ThemeManager
) : FileListContract.UserAction {

    private var rootPath = fileProviderRootManager.getFileRootPath(FileProvider.Local)
    private var currentPath = rootPath
    private val fileChildrenResultListener = createFileChildrenResultListener()
    private val themeListener = createThemeListener()

    override fun onAttached() {
        fileChildrenManager.registerFileChildrenResultListener(fileChildrenResultListener)
        syncFileChildren()
        themeManager.registerThemeListener(themeListener)
        syncWithCurrentTheme()
    }

    override fun onDetached() {
        fileChildrenManager.unregisterFileChildrenResultListener(fileChildrenResultListener)
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onRefresh() {
        val fileChildrenResult = fileChildrenManager.loadFileChildren(currentPath, true)
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
        val parentPath = filePathManager.getParentPath(currentPath) ?: return
        currentPath = parentPath
        screen.notifyListenerCurrentPathChanged(currentPath)
        syncFileChildren()
    }

    override fun onSetFileManagers(
        fileChildrenManager: FileChildrenManager,
        fileOpenManager: FileOpenManager,
        rootPath: String
    ) {
        this.fileChildrenManager.unregisterFileChildrenResultListener(fileChildrenResultListener)
        this.fileChildrenManager = fileChildrenManager
        this.fileOpenManager = fileOpenManager
        this.rootPath = rootPath
        this.currentPath = rootPath
        fileChildrenManager.registerFileChildrenResultListener(fileChildrenResultListener)
    }

    override fun getCurrentPath() = currentPath

    private fun syncFileChildren() {
        var fileChildrenResult = fileChildrenManager.getFileChildren(currentPath)
        if (fileChildrenResult.status == FileChildrenResult.Status.UNLOADED ||
            fileChildrenResult.status == FileChildrenResult.Status.ERROR_NOT_FOLDER) {
            fileChildrenResult = fileChildrenManager.loadFileChildren(currentPath)
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

    private fun createFileChildrenResultListener() = object : FileChildrenManager.FileChildrenResultListener {
        override fun onFileChildrenResultChanged(path: String) {
            if (currentPath != path) {
                return
            }
            val fileChildren = fileChildrenManager.getFileChildren(currentPath)
            syncFileChildren(fileChildren)
        }
    }
}
