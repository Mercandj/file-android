package com.mercandalli.android.apps.files.file_list

import com.mercandalli.android.sdk.files.api.FileChildrenResult
import com.mercandalli.android.sdk.files.api.FileManager

class FileListPresenter(
        private val screen: FileListContract.Screen,
        private val fileManager: FileManager
) : FileListContract.UserAction {

    private val fileChildrenResultListener = createFileChildrenResultListener()
    private var currentPath = "/"

    override fun onAttached() {
        fileManager.registerFileChildrenResultListener(fileChildrenResultListener)
        var fileChildren = fileManager.getFileChildren(currentPath)
        if (fileChildren.status == FileChildrenResult.UNLOADED) {
            fileChildren = fileManager.loadFileChildren(currentPath)
        }
        syncFileChildren(fileChildren)
    }

    override fun onDetached() {
        fileManager.unregisterFileChildrenResultListener(fileChildrenResultListener)
    }

    private fun syncFileChildren(
            fileChildrenResult: FileChildrenResult = fileManager.getFileChildren(currentPath)) {
        when (fileChildrenResult.status) {
            FileChildrenResult.UNLOADED -> {
                screen.hideEmptyView()
                screen.showErrorMessage()
                screen.hideFiles()
                screen.hideLoader()
            }
            FileChildrenResult.LOADING -> {
                screen.hideEmptyView()
                screen.hideErrorMessage()
                screen.hideFiles()
                screen.showLoader()
            }
            FileChildrenResult.LOADED -> {
                val files = fileChildrenResult.getFiles()
                screen.hideErrorMessage()
                screen.hideLoader()
                if (files.isEmpty()) {
                    screen.showEmptyView()
                    screen.hideFiles()
                } else {
                    screen.hideEmptyView()
                    screen.showFiles(files)
                }
            }
        }
    }

    private fun createFileChildrenResultListener(): FileManager.FileChildrenResultListener {
        return object : FileManager.FileChildrenResultListener {
            override fun onFileChildrenResultChanged(path: String) {
                if (currentPath != path) {
                    return
                }
                syncFileChildren()
            }
        }
    }
}