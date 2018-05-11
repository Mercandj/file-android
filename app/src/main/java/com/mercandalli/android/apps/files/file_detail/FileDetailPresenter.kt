package com.mercandalli.android.apps.files.file_detail

import android.annotation.SuppressLint
import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.sdk.files.api.File

class FileDetailPresenter(
        private val screen: FileDetailContract.Screen,
        private val audioManager: AudioManager,
        private val playStringRes: Int,
        private val pauseStringRes: Int
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
            screen.hidePlayPauseButton()
            return
        }
        if (file.directory) {
            throw IllegalStateException("Directory not supported for now")
        }
        screen.setTitle(file.name)
        screen.setPath(file.path)
        screen.setLength(humanReadableByteCount(file.length))
        if (audioManager.isSupportedPath(file.path)) {
            screen.showPlayPauseButton()
        } else {
            screen.hidePlayPauseButton()
        }
        synchronizePlayButton()
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

    private fun synchronizePlayButton() {
        if (file == null) {
            return
        }
        val sourcePath = audioManager.getSourcePath()
        if (file!!.path != sourcePath) {
            screen.setPlayPauseButtonText(playStringRes)
            return
        }
        val playing = audioManager.isPlaying()
        if (playing) {
            screen.setPlayPauseButtonText(pauseStringRes)
        } else {
            screen.setPlayPauseButtonText(playStringRes)
        }
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