@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_column_row

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.toast.ToastManager
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
    private val toastManager: ToastManager,
    @DrawableRes
    private val drawableRightIconDirectoryDrawableRes: Int,
    @DrawableRes
    private val drawableRightIconSoundDrawableRes: Int,
    @ColorRes
    private val selectedTextColorRes: Int,
    @ColorRes
    private val selectedBackgroundColorRes: Int
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
        syncWithCurrentTheme()
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
        if (fileName.contains("/")) {
            toastManager.toast("File name should not contain /")
        }
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
        val theme = themeManager.getTheme()
        screen.setTextColorRes(if (selected) selectedTextColorRes else theme.textPrimaryColorRes)
        screen.setBackgroundColorRes(if (selected) selectedBackgroundColorRes else theme.cardBackgroundColorRes)
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
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
