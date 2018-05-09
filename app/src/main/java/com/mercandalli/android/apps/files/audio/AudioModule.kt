package com.mercandalli.android.apps.files.audio

import android.media.MediaPlayer

class AudioModule {

    fun provideAudioManager():AudioManager {
        val mediaPlayer = MediaPlayer()
        return AudioManagerMediaPlayer(
                mediaPlayer
        )
    }
}