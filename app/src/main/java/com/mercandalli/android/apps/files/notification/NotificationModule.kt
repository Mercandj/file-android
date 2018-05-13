package com.mercandalli.android.apps.files.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.RemoteViews
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.audio.AudioQueueManager

class NotificationModule(
        private val context: Context,
        private val audioManager: AudioManager,
        private val audioQueueManager: AudioQueueManager
) {

    fun provideNotificationAudioManager(): NotificationAudioManager {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val addOn = object : NotificationAudioManagerImpl.AddOn {
            override fun createNotification(notificationId: Int) {
                val channelId = "filespace_notification_channel_audio"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val name = "FileSpace audio channel"
                    val description = "FileSpace audio notification channel"
                    val importance = NotificationManager.IMPORTANCE_LOW
                    val channel = NotificationChannel(channelId, name, importance)
                    channel.description = description
                    notificationManager.createNotificationChannel(channel)
                }

                val remoteViews = RemoteViews(context.packageName, R.layout.view_notification_audio)
                /*
                remoteViews.setTextViewText(R.id.titre_notif,
                        mCurrentMusic!!.getName())
                remoteViews.setOnClickPendingIntent(R.id.titre_notif,
                        getNotificationIntentActivity(context))
                remoteViews.setOnClickPendingIntent(R.id.close,
                        getNotificationIntentClose(context))
                remoteViews.setOnClickPendingIntent(R.id.activity_file_audio_play,
                        getNotificationIntentPlayPause(context))
                remoteViews.setOnClickPendingIntent(R.id.activity_file_audio_next,
                        getNotificationIntentNext(context))
                remoteViews.setOnClickPendingIntent(R.id.prev,
                        getNotificationIntentPrevious(context))
                        */
                NotificationManagerCompat.from(context).notify(notificationId,
                        NotificationCompat.Builder(context, channelId)
                                .setSmallIcon(R.drawable.ic_sd_storage_black_24dp)
                                .setAutoCancel(false)
                                //.setOngoing(true)
                                .setContent(remoteViews)
                                /*
                                .addAction(R.mipmap.ic_launcher,
                                        "Next",
                                        getNotificationIntentNext(context))
                                .addAction(R.mipmap.ic_launcher,
                                        "Play/Pause",
                                        getNotificationIntentPlayPause(context))
                                .addAction(R.mipmap.ic_launcher,
                                        "Previous",
                                        getNotificationIntentPrevious(context))
                                .extend(NotificationCompat.WearableExtender()
                                        .setBackground(BitmapUtils.drawableToBitmap(
                                                ContextCompat.getDrawable(
                                                        context,
                                                        R.drawable.ic_music_note_white_24dp))))
                                .setDeleteIntent(getNotificationIntentPause(context))
                                .setContentIntent(PendingIntent.getActivity(context, 0,
                                        Intent(context, FileAudioActivity::class.java),
                                        PendingIntent.FLAG_UPDATE_CURRENT))
                                        */
                                .build())
            }

            override fun cancelNotification(notificationId: Int) {
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
        }
        return NotificationAudioManagerImpl(
                audioManager,
                audioQueueManager,
                addOn
        )
    }
}