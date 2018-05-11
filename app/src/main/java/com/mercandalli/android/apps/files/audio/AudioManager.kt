package com.mercandalli.android.apps.files.audio

interface AudioManager {

    fun play()

    fun pause()

    fun isPlaying(): Boolean

    fun reset()

    fun getSourcePath(): String?

    fun setSourcePath(path: String?)

    fun prepareAsync()

    fun isSupportedPath(path: String): Boolean

    fun getProgressPercent(): Float

    fun registerSourceListener(listener: SourceListener)

    fun unregisterSourceListener(listener: SourceListener)

    fun registerPlayListener(listener: PlayListener)

    fun unregisterPlayListener(listener: PlayListener)

    fun registerCompletionListener(listener: CompletionListener)

    fun unregisterCompletionListener(listener: CompletionListener)

    interface SourceListener {
        fun onAudioSourceChanged()
    }

    interface PlayListener {
        fun onPlayPauseChanged()
    }

    interface CompletionListener {
        fun onCompleted()
    }

    interface ProgressListener {
        fun onProgressChanged()
    }
}