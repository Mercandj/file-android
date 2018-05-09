package com.mercandalli.android.apps.files.audio

import android.media.MediaPlayer

class AudioManagerMediaPlayer(
        private val mediaPlayer: MediaPlayer
) : AudioManager {

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun setSource(path: String) {
        mediaPlayer.setDataSource(path)
    }

    override fun prepareAsync() {
        mediaPlayer.prepareAsync()
    }
}