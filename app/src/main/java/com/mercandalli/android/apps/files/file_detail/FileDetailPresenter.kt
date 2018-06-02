package com.mercandalli.android.apps.files.file_detail

import android.annotation.SuppressLint
import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.audio.AudioQueueManager
import com.mercandalli.sdk.files.api.*
import java.util.*

class FileDetailPresenter(
        private val screen: FileDetailContract.Screen,
        private val audioManager: AudioManager,
        private val audioQueueManager: AudioQueueManager,
        private val fileOpenManager: FileOpenManager,
        private val fileDeleteManager: FileDeleteManager,
        private val fileCopyCutManager: FileCopyCutManager,
        private val fileRenameManager: FileRenameManager,
        private val fileShareManager: FileShareManager,
        private val playDrawableRes: Int,
        private val pauseDrawableRes: Int
) : FileDetailContract.UserAction {

    private val playListener = createPlayListener()
    private var file: File? = null

    override fun onAttached() {
        audioManager.registerPlayListener(playListener)
        synchronizePlayButton()
    }

    override fun onDetached() {
        audioManager.unregisterPlayListener(playListener)
    }

    override fun onFileChanged(file: File?) {
        this.file = file
        if (file == null) {
            screen.setTitle("")
            screen.setPath("")
            screen.setLastModified("")
            screen.hidePlayPauseButton()
            return
        }
        val (_, path, _, directory, name, length, lastModified) = file
        if (directory) {
            throw IllegalStateException("Directory not supported for now")
        }
        screen.setTitle(name)
        screen.setPath(path)
        screen.setLength(humanReadableByteCount(length))
        screen.setLastModified(Date(lastModified).toString())
        if (audioManager.isSupportedPath(path)) {
            screen.showPlayPauseButton()
            screen.showNextButton()
            screen.showPreviousButton()
        } else {
            screen.hidePlayPauseButton()
            screen.hideNextButton()
            screen.hidePreviousButton()
        }
        synchronizePlayButton()
    }

    override fun onOpenClicked() {
        fileOpenManager.open(file!!.path)
    }

    override fun onPlayPauseClicked() {
        if (audioManager.getSourcePath() != file!!.path) {
            audioManager.reset()
            audioManager.setSourcePath(file!!.path)
            audioManager.prepareAsync()
            return
        }
        val playing = audioManager.isPlaying()
        if (playing) {
            audioManager.pause()
        } else {
            audioManager.play()
        }
    }

    override fun onNextClicked() {
        val nextPath = audioQueueManager.next(file!!.path)
        audioManager.reset()
        audioManager.setSourcePath(nextPath)
        audioManager.prepareAsync()
    }

    override fun onPreviousClicked() {
        val previousPath = audioQueueManager.previous(file!!.path)
        audioManager.reset()
        audioManager.setSourcePath(previousPath)
        audioManager.prepareAsync()
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

    override fun onShareClicked() {
        fileShareManager.share(file!!.path)
    }

    override fun onCopyClicked() {
        fileCopyCutManager.copy(file!!.path)
    }

    override fun onCutClicked() {
        fileCopyCutManager.cut(file!!.path)
    }

    private fun synchronizePlayButton() {
        if (file == null) {
            return
        }
        val sourcePath = audioManager.getSourcePath()
        if (file!!.path != sourcePath) {
            screen.setPlayPauseButtonImage(playDrawableRes)
            return
        }
        val playing = audioManager.isPlaying()
        screen.setPlayPauseButtonImage(if (playing) pauseDrawableRes else playDrawableRes)
    }

    private fun createPlayListener(): AudioManager.PlayListener {
        return object : AudioManager.PlayListener {
            override fun onPlayPauseChanged() {
                synchronizePlayButton()
            }
        }
    }

    companion object {

        fun humanReadableByteCount(bytes: Long): String {
            return humanReadableByteCount(bytes, true)
        }

        @SuppressLint("DefaultLocale")
        private fun humanReadableByteCount(bytes: Long, si: Boolean): String {
            val unit = if (si) 1000 else 1024
            if (bytes < unit) {
                return bytes.toString() + " B"
            }
            val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
            val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
            return String.format("%.1f %sB", bytes / Math.pow(unit.toDouble(), exp.toDouble()), pre)
        }
    }
}