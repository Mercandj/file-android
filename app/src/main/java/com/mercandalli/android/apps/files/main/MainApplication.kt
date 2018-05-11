package com.mercandalli.android.apps.files.main

import android.app.Application
import com.mercandalli.android.apps.files.audio.AudioManager

/**
 * The [Application] of this project.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        ApplicationGraph.init(this)

        val audioManager = ApplicationGraph.getAudioManager()
        val audioQueueManager = ApplicationGraph.getAudioQueueManager()
        audioManager.registerCompletionListener(object : AudioManager.CompletionListener {
            override fun onCompleted() {
                val sourcePath = audioManager.getSourcePath() ?: return
                val nextPath = audioQueueManager.next(sourcePath)
                audioManager.reset()
                audioManager.setSourcePath(nextPath)
                audioManager.prepareAsync()
            }
        })
    }
}