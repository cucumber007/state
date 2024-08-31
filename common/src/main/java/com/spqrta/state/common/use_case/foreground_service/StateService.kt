package com.spqrta.state.common.use_case.foreground_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat

class StateService : Service() {

    private val notificationManager by lazy { NotificationManagerCompat.from(this) }
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.getBooleanExtra(KEY_START, false)) {
            true -> startForeground(1, createNotification())
            false -> stopForeground(STOP_FOREGROUND_REMOVE)
        }
        return Service.START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("State")
            .setContentText("Service is running")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "foreground_notification"
        private const val CHANNEL_NAME = "Foreground Notification"
        const val KEY_START = "start"

        fun createStartIntent(context: Context): Intent {
            return Intent(context, StateService::class.java)
                .putExtra(KEY_START, true)
        }

        fun createStopIntent(context: Context): Intent {
            return Intent(context, StateService::class.java)
                .putExtra(KEY_START, false)
        }
    }
}
