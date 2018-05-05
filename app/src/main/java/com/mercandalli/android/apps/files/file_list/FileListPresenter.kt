package com.mercandalli.android.apps.files.file_list

import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileManager

class FileListPresenter(
        private val screen: FileListContract.Screen,
        private val fileManager: FileManager,
        private var currentPath: String
) : FileListContract.UserAction {

    private val fileChildrenResultListener = createFileChildrenResultListener()

    override fun onAttached() {
        fileManager.registerFileChildrenResultListener(fileChildrenResultListener)
        syncFileChildren()
    }

    override fun onDetached() {
        fileManager.unregisterFileChildrenResultListener(fileChildrenResultListener)
    }

    override fun onResume() {
        syncFileChildren()
    }

    override fun onRefresh() {
        val fileChildrenResult = fileManager.loadFileChildren(currentPath, true)
        syncFileChildren(fileChildrenResult)
    }

    private fun syncFileChildren() {
        var fileChildrenResult = fileManager.getFileChildren(currentPath)
        if (fileChildrenResult.status == FileChildrenResult.UNLOADED ||
                fileChildrenResult.status == FileChildrenResult.ERROR_NOT_FOLDER) {
            fileChildrenResult = fileManager.loadFileChildren(currentPath)
        }
        syncFileChildren(fileChildrenResult)
    }

    private fun syncFileChildren(fileChildrenResult: FileChildrenResult) {
        when (fileChildrenResult.status) {
            FileChildrenResult.UNLOADED, FileChildrenResult.ERROR_NOT_FOLDER -> {
                screen.hideEmptyView()
                screen.showErrorMessage()
                screen.hideFiles()
                screen.hideLoader()
            }
            FileChildrenResult.LOADING -> {
                screen.hideEmptyView()
                screen.hideErrorMessage()
                screen.showLoader()
            }
            FileChildrenResult.LOADED_SUCCEEDED -> {
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
                val fileChildren = fileManager.getFileChildren(currentPath)
                syncFileChildren(fileChildren)
            }
        }
    }
}