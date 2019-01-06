@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_column_list

import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileSortManager

class FileColumnListPresenter(
    private val screen: FileColumnListContract.Screen,
    private var fileChildrenManager: FileChildrenManager,
    private val fileSortManager: FileSortManager,
    private val themeManager: ThemeManager,
    private var currentPath: String
) : FileColumnListContract.UserAction {

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

    override fun onResume() {
        syncFileChildren()
    }

    override fun onRefresh() {
        val fileChildrenResult = fileChildrenManager.loadFileChildren(currentPath, true)
        syncFileChildren(fileChildrenResult)
    }

    override fun onPathChanged(path: String) {
        currentPath = path
        syncFileChildren()
    }

    override fun onPathSelected(path: String?) {
        screen.selectPath(path)
    }

    override fun onSetFileManager(fileChildrenManager: FileChildrenManager) {
        this.fileChildrenManager.unregisterFileChildrenResultListener(fileChildrenResultListener)
        this.fileChildrenManager = fileChildrenManager
        fileChildrenManager.registerFileChildrenResultListener(fileChildrenResultListener)
    }

    private fun syncFileChildren() {
        var fileChildrenResult = fileChildrenManager.getFileChildren(currentPath)
        if (fileChildrenResult.status == FileChildrenResult.Status.UNLOADED ||
            fileChildrenResult.status == FileChildrenResult.Status.ERROR_NOT_FOLDER ||
            fileChildrenResult.status == FileChildrenResult.Status.ERROR_NETWORK) {
            fileChildrenResult = fileChildrenManager.loadFileChildren(currentPath)
        }
        syncFileChildren(fileChildrenResult)
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
