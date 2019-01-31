package com.mercandalli.android.apps.files.main

import com.mercandalli.android.apps.files.toast.ToastManager
import com.mercandalli.android.apps.files.version.VersionManager
import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileOpenManager
import com.mercandalli.sdk.files.api.FileSortManager

class MainFragmentPresenter(
    private val screen: MainFragmentContract.Screen,
    private val fileChildrenManager: FileChildrenManager,
    private val fileOpenManager: FileOpenManager,
    private val fileSortManager: FileSortManager,
    private val toastManager: ToastManager,
    private val versionManager: VersionManager,
    rootPath: String
) : MainFragmentContract.UserAction {

    private val fileChildrenResultListener = createFileChildrenResultListener()
    private val paths = ArrayList<String>()

    init {
        paths.add(rootPath)
    }

    override fun onCreate() {
        fileChildrenManager.registerFileChildrenResultListener(fileChildrenResultListener)
        loadFiles()
        syncFiles()
    }

    override fun onDestroy() {
        fileChildrenManager.unregisterFileChildrenResultListener(fileChildrenResultListener)
    }

    override fun onFileClicked(mainFileViewModel: MainFileViewModel) {
        val path = mainFileViewModel.path
        val directory = mainFileViewModel.directory
        if (directory) {
            var i = paths.size - 1
            while (!path.startsWith(paths[i]) && i >= 0) {
                paths.removeAt(i)
                i--
            }
            paths.add(path)
            loadFiles()
            syncFiles()
            return
        }
        if (path.toLowerCase().endsWith("html")) {
            toastManager.toast("Web content not supported for now")
            return
        }
        fileOpenManager.open(path)
    }

    override fun onSettingsClicked() {
        val versionName = versionManager.getVersionName()
        toastManager.toast("Version: $versionName")
    }

    private fun loadFiles() {
        for (path in paths) {
            fileChildrenManager.loadFileChildren(path)
        }
    }

    private fun syncFiles() {
        val mainFileRows = ArrayList<MainFileRowViewModel>()
        for (path in paths) {
            val fileChildren = fileChildrenManager.getFileChildren(path)
            val status = fileChildren.status
            when (status) {
                FileChildrenResult.Status.ERROR_NOT_FOLDER -> TODO()
                FileChildrenResult.Status.ERROR_NETWORK -> TODO()
                FileChildrenResult.Status.UNLOADED -> {
                }
                FileChildrenResult.Status.LOADING -> {
                }
                FileChildrenResult.Status.LOADED_SUCCEEDED -> {
                    val files = fileChildren.getFiles()
                    val filesSorted = fileSortManager.sort(files)
                    mainFileRows.add(
                        MainFileRowViewModel.create(
                            "Local Files: $path",
                            filesSorted
                        )
                    )
                }
            }
        }
        screen.showFiles(mainFileRows)
    }

    private fun createFileChildrenResultListener() = object : FileChildrenManager.FileChildrenResultListener {
        override fun onFileChildrenResultChanged(path: String) {
            if (!paths.contains(path)) {
                return
            }
            syncFiles()
        }
    }
}
