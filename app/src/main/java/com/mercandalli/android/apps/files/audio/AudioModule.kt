package com.mercandalli.android.apps.files.audio

import android.media.MediaPlayer
import com.mercandalli.sdk.files.api.FileSortManager

class AudioModule(
        private val fileSortManager: FileSortManager
) {

    fun createAudioManager(): AudioManager {
        val mediaPlayer = MediaPlayer()
        return AudioManagerMediaPlayer(
                object : AudioManagerMediaPlayer.MediaPlayerWrapper {
                    override val isPlaying: Boolean
                        get() = mediaPlayer.isPlaying
                    override val currentPosition: Int
                        get() = mediaPlayer.currentPosition
                    override val duration: Int
                        get() = mediaPlayer.duration

                    override fun setOnPreparedListener(function: () -> Unit) {
                        mediaPlayer.setOnPreparedListener {
                            function()
                        }
                    }

                    override fun setOnCompletionListener(function: () -> Unit) {
                        mediaPlayer.setOnCompletionListener {
                            function()
                        }
                    }

                    override fun start() {
                        mediaPlayer.start()
                    }

                    override fun pause() {
                        mediaPlayer.pause()
                    }

                    override fun reset() {
                        mediaPlayer.reset()
                    }

                    override fun setDataSource(path: String) {
                        mediaPlayer.setDataSource(path)
                    }

                    override fun prepareAsync() {
                        mediaPlayer.prepareAsync()
                    }
                }
        )
    }

    fun createAudioQueueManager(audioManager: AudioManager): AudioQueueManager {
        return AudioQueueManagerImpl(
                audioManager,
                fileSortManager
        )
    }
}