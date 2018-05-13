package com.mercandalli.android.apps.files.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.mercandalli.android.apps.files.main.ApplicationGraph

/**
 * A [BroadcastReceiver] to receive notification actions.
 */
class NotificationAudioReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val audioManager = ApplicationGraph.getAudioManager()
        val audioQueueManager = ApplicationGraph.getAudioQueueManager()
        val notificationAudioManager = ApplicationGraph.getNotificationAudioManager()
        val currentAudioSourcePath = audioManager.getSourcePath()
        if (currentAudioSourcePath == null) {
            notificationAudioManager.hideNotification()
            return
        }
        when (action) {
            ACTION_PLAY_PAUSE -> if (audioManager.isPlaying()) {
                audioManager.pause()
            } else {
                audioManager.play()
            }
            ACTION_PAUSE -> audioManager.pause()
            ACTION_NEXT -> {
                val nextPath = audioQueueManager.next(currentAudioSourcePath)
                audioManager.reset()
                audioManager.setSourcePath(nextPath)
                audioManager.prepareAsync()
            }
            ACTION_PREVIOUS -> {
                val previousPath = audioQueueManager.next(currentAudioSourcePath)
                audioManager.reset()
                audioManager.setSourcePath(previousPath)
                audioManager.prepareAsync()
            }
            ACTION_CLOSE -> {
                if (audioManager.isPlaying()) {
                    audioManager.pause()
                }
                notificationAudioManager.hideNotification()
            }
        }
    }

    companion object {

        /**
         * The action start the app.
         */
        private const val ACTION_ACTIVITY = "NotificationAudioReceiver.Actions.ACTION_ACTIVITY"

        /**
         * The action play or pause.
         */
        private const val ACTION_PLAY_PAUSE = "NotificationAudioReceiver.Actions.ACTION_PLAY_PAUSE"

        /**
         * The action pause.
         */
        private const val ACTION_PAUSE = "NotificationAudioReceiver.Actions.ACTION_PAUSE"

        /**
         * The action previous.
         */
        private const val ACTION_PREVIOUS = "NotificationAudioReceiver.Actions.ACTION_PREVIOUS"

        /**
         * The action next.
         */
        private const val ACTION_NEXT = "NotificationAudioReceiver.Actions.ACTION_NEXT"

        /**
         * The action close.
         */
        private const val ACTION_CLOSE = "NotificationAudioReceiver.Actions.ACTION_CLOSE"

        @JvmStatic
        fun getNotificationIntentActivity(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_ACTIVITY)
        }

        @JvmStatic
        fun getNotificationIntentPlayPause(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_PLAY_PAUSE)
        }

        @JvmStatic
        fun getNotificationIntentPause(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_PAUSE)
        }

        @JvmStatic
        fun getNotificationIntentPrevious(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_PREVIOUS)
        }

        @JvmStatic
        fun getNotificationIntentNext(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_NEXT)
        }

        @JvmStatic
        fun getNotificationIntentClose(context: Context): PendingIntent {
            return getPendingIntent(context, ACTION_CLOSE)
        }

        private fun getPendingIntent(
                context: Context,
                action: String): PendingIntent {
            val intent = Intent(context, NotificationAudioReceiver::class.java)
            intent.action = action
            return PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}
