package com.mercandalli.android.apps.files.file_row

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileRenameManager

class FileRowPresenter(
        private val screen: FileRowContract.Screen,
        private val fileDeleteManager: FileDeleteManager,
        private val fileCopyCutManager: FileCopyCutManager,
        private val fileRenameManager: FileRenameManager
) : FileRowContract.UserAction {

    private var file: File? = null
    private var selected = false

    override fun onFileChanged(file: File, selectedPath: String?) {
        this.file = file
        screen.setTitle(file.name)
        val directory = file.directory
        screen.setArrowRightVisibility(directory)
        screen.setIcon(directory)
        selected = isSelected(file.path, selectedPath)
        screen.setRowSelected(selected)
    }

    override fun onRowClicked() {
        screen.notifyRowClicked(file!!)
    }

    override fun onRowLongClicked() {
        screen.showOverflowPopupMenu()
        screen.notifyRowLongClicked(file!!)
    }

    override fun onCopyClicked() {
        fileCopyCutManager.copy(file!!.path)
    }

    override fun onCutClicked() {
        fileCopyCutManager.cut(file!!.path)
    }

    override fun onDeleteClicked() {
        screen.showDeleteConfirmation(file!!.name)
    }

    override fun onDeleteConfirmedClicked() {
        fileDeleteManager.delete(file!!.path)
    }

    override fun onRenameClicked() {
        screen.showRenamePrompt(file!!.name)
    }

    override fun onRenameConfirmedClicked(fileName: String) {
        fileRenameManager.rename(file!!.path, fileName)
    }

    companion object {

        @JvmStatic
        private fun isSelected(filePath: String, selectedPath: String?): Boolean {
            val startWith = selectedPath?.startsWith(filePath) ?: false
            if (startWith && selectedPath != null) {
                val removePrefix = selectedPath.removePrefix(filePath)
                if (removePrefix != "" && !removePrefix.startsWith('/')) {
                    return false
                }
            }
            return startWith
        }
    }
}