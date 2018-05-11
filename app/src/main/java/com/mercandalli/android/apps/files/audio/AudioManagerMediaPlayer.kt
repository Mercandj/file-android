package com.mercandalli.android.apps.files.audio

class AudioManagerMediaPlayer(
        private val mediaPlayer: MediaPlayerWrapper
) : AudioManager {

    private val sourceListeners = ArrayList<AudioManager.SourceListener>()
    private val playListeners = ArrayList<AudioManager.PlayListener>()
    private val completionListeners = ArrayList<AudioManager.CompletionListener>()
    private var path: String? = null

    init {
        mediaPlayer.setOnCompletionListener {
            for (listener in playListeners) {
                listener.onPlayPauseChanged()
            }
            for (listener in completionListeners) {
                listener.onCompleted()
            }
            path = null
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

    override fun getProgressPercent(): Float {
        return mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration
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

    override fun registerCompletionListener(listener: AudioManager.CompletionListener) {
        if (completionListeners.contains(listener)) {
            return
        }
        completionListeners.add(listener)
    }

    override fun unregisterCompletionListener(listener: AudioManager.CompletionListener) {
        completionListeners.remove(listener)
    }

    interface MediaPlayerWrapper {
        val isPlaying: Boolean
        val currentPosition: Int
        val duration: Int
        fun setOnPreparedListener(function: () -> Unit)
        fun setOnCompletionListener(function: () -> Unit)
        fun start()
        fun pause()
        fun reset()
        fun setDataSource(path: String)
        fun prepareAsync()
    }
}