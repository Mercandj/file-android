package com.mercandalli.android.apps.files.notification

import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.audio.AudioQueueManager

class NotificationAudioManagerImpl(
        private val audioManager: AudioManager,
        private val audioQueueManager: AudioQueueManager,
        private val addOn: AddOn
) : NotificationAudioManager {

    override fun initialize() {
        audioManager.registerSourceListener(object : AudioManager.SourceListener {
            override fun onAudioSourceChanged() {
                synchronizeNotificationWithAudioSource()
            }
        })
    }

    private fun synchronizeNotificationWithAudioSource() {
        val sourcePath = audioManager.getSourcePath()
        if (sourcePath == null) {
            addOn.cancelNotification(NOTIFICATION_AUDIO_ID)
            return
        }
        addOn.createNotification(NOTIFICATION_AUDIO_ID)
    }

    companion object {
        private const val NOTIFICATION_AUDIO_ID = 42
    }

    interface AddOn {

        fun createNotification(notificationId: Int)

        fun cancelNotification(notificationId: Int)
    }
}