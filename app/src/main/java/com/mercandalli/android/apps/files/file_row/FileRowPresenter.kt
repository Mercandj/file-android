package com.mercandalli.android.apps.files.file_row

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileDeleteManager

class FileRowPresenter(
        private val screen: FileRowContract.Screen,
        private val fileDeleteManager: FileDeleteManager
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

    override fun onDeleteClicked() {
        fileDeleteManager.delete(file!!.path)
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