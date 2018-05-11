package com.mercandalli.android.apps.files.audio

import android.media.MediaPlayer

class AudioManagerMediaPlayer(
        private val mediaPlayer: MediaPlayer
) : AudioManager {

    private val sourceListeners = ArrayList<AudioManager.SourceListener>()
    private val playListeners = ArrayList<AudioManager.PlayListener>()
    private var path: String? = null

    init {
        mediaPlayer.setOnCompletionListener {
            for (listener in playListeners) {
                listener.onPlayPauseChanged()
            }
        }
        mediaPlayer.setOnPreparedListener {
            play()
        }
    }

    override fun play() {
        mediaPlayer.start()
        for (listener in playListeners) {
            listener.onPlayPauseChanged()
        }
    }

    override fun pause() {
        mediaPlayer.pause()
        for (listener in playListeners) {
            listener.onPlayPauseChanged()
        }
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun reset() {
        mediaPlayer.reset()
    }

    override fun getSourcePath(): String? {
        return path
    }

    override fun setSourcePath(path: String?) {
        this.path = path
        if (path == null) {
            mediaPlayer.reset()
        } else {
            mediaPlayer.setDataSource(path)
        }
        for (listener in sourceListeners) {
            listener.onAudioSourceChanged()
        }
    }

    override fun prepareAsync() {
        mediaPlayer.prepareAsync()
    }

    override fun isSupportedPath(path: String): Boolean {
        val pathLowerCase = path.toLowerCase()
        return pathLowerCase.endsWith(".mp3") || pathLowerCase.endsWith(".wav")
    }

    override fun registerSourceListener(listener: AudioManager.SourceListener) {
        if (sourceListeners.contains(listener)) {
            return
        }
        sourceListeners.add(listener)
    }

    override fun unregisterSourceListener(listener: AudioManager.SourceListener) {
        sourceListeners.remove(listener)
    }

    override fun registerPlayListener(listener: AudioManager.PlayListener) {
        if (playListeners.contains(listener)) {
            return
        }
        playListeners.add(listener)
    }

    override fun unregisterPlayListener(listener: AudioManager.PlayListener) {
        playListeners.remove(listener)
    }
}