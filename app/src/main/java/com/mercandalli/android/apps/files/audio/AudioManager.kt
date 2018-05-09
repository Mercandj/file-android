package com.mercandalli.android.apps.files.audio

interface AudioManager {

    fun play()

    fun pause()

    fun isPlaying(): Boolean

    fun setSource(path: String)

    fun prepareAsync()
}