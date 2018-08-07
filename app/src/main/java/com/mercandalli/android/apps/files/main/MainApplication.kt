package com.mercandalli.android.apps.files.main

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.mercandalli.android.apps.files.BuildConfig
import com.mercandalli.android.apps.files.audio.AudioManager
import io.fabric.sdk.android.Fabric

/**
 * The [Application] of this project.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Setup fabric
        setupCrashlytics()

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

        val notificationAudioManager = ApplicationGraph.getNotificationAudioManager()
        notificationAudioManager.initialize()
    }

    private fun setupCrashlytics() {
        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()
        Fabric.with(this, crashlyticsKit)
    }
}