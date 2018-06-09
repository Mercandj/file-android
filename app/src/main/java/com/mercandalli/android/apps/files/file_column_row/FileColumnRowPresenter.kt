package com.mercandalli.android.apps.files.file_column_row

import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileRenameManager

class FileColumnRowPresenter(
        private val screen: FileColumnRowContract.Screen,
        private val fileDeleteManager: FileDeleteManager,
        private val fileCopyCutManager: FileCopyCutManager,
        private val fileRenameManager: FileRenameManager,
        private val audioManager: AudioManager,
        private val themeManager: ThemeManager,
        private val drawableRightIconDirectoryDrawableRes: Int,
        private val drawableRightIconSoundDrawableRes: Int,
        private val selectedTextColorRes: Int
) : FileColumnRowContract.UserAction {

    private val playListener = createPlayListener()
    private val themeListener = createThemeListener()
    private var file: File? = null
    private var selected = false

    override fun onAttached() {
        audioManager.registerPlayListener(playListener)
        synchronizeRightIcon()
        themeManager.registerThemeListener(themeListener)
        syncWithCurrentTheme()
    }

    override fun onDetached() {
        audioManager.unregisterPlayListener(playListener)
        themeManager.unregisterThemeListener(themeListener)
    }

    override fun onFileChanged(file: File, selectedPath: String?) {
        this.file = file
        screen.setTitle(file.name)
        val directory = file.directory
        screen.setRightIconVisibility(directory)
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

    private fun synchronizeRightIcon() {
        if (file == null) {
            return
        }
        if (file!!.directory) {
            screen.setRightIconVisibility(true)
            screen.setRightIconDrawableRes(drawableRightIconDirectoryDrawableRes)
            return
        }
        val sourcePath = audioManager.getSourcePath()
        if (sourcePath == null || file!!.path != sourcePath) {
            screen.setRightIconVisibility(false)
            return
        }
        val playing = audioManager.isPlaying()
        if (playing) {
            screen.setRightIconVisibility(true)
            screen.setRightIconDrawableRes(drawableRightIconSoundDrawableRes)
        } else {
            screen.setRightIconVisibility(false)
        }
    }

    private fun createPlayListener() = object : AudioManager.PlayListener {
        override fun onPlayPauseChanged() {
            synchronizeRightIcon()
        }
    }

    private fun syncWithCurrentTheme() {
        val theme = themeManager.theme
        screen.setTextColorRes(if (selected) selectedTextColorRes else theme.textPrimaryColorRes)
    }

    private fun createThemeListener() = object : ThemeManager.OnCurrentThemeChangeListener {
        override fun onCurrentThemeChanged() {
            syncWithCurrentTheme()
        }
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