package com.mercandalli.android.apps.files.main

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.android.play.core.splitcompat.SplitCompat
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

        initializeAudio()
        initializeNotificationAudio()
        initializeProduct()
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        SplitCompat.install(this)
    }

    private fun setupCrashlytics() {
        val crashlyticsKit = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build()
        Fabric.with(this, crashlyticsKit)
    }

    companion object {

        private fun initializeAudio() {
            val audioManager = ApplicationGraph.getAudioManager()
            val audioQueueManager = ApplicationGraph.getAudioQueueManager()
            val audioManagerCompletionListener = object : AudioManager.CompletionListener {
                override fun onCompleted() {
                    val sourcePath = audioManager.getSourcePath() ?: return
                    val nextPath = audioQueueManager.next(sourcePath)
                    audioManager.reset()
                    audioManager.setSourcePath(nextPath)
                    audioManager.prepareAsync()
                }
            }
            audioManager.registerCompletionListener(audioManagerCompletionListener)
        }

        private fun initializeNotificationAudio() {
            val notificationAudioManager = ApplicationGraph.getNotificationAudioManager()
            notificationAudioManager.initialize()
        }

        private fun initializeProduct() {
            val productManager = ApplicationGraph.getProductManager()
            productManager.initialize()
        }
    }
}
