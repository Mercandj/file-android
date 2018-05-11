package com.mercandalli.android.apps.files.audio

import android.media.MediaPlayer

class AudioModule {

    fun provideAudioManager(): AudioManager {
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

    fun provideAudioQueueManager(audioManager: AudioManager): AudioQueueManager {
        return AudioQueueManagerImpl(
                audioManager
        )
    }
}