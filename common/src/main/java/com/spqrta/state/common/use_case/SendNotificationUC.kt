package com.spqrta.state.common.use_case

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.effect.SendNotificationEffect
import com.spqrta.state.common.util.noActions
import com.spqrta.state.common.util.toFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat

@Suppress("OPT_IN_USAGE")
class SendNotificationUC(
    private val context: Context,
    private val showToastUC: ShowToastUC,
) {

    fun flow(effect: SendNotificationEffect): Flow<List<AppAction>> {
        return {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "default"
                val channelName = "Default"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, channelName, importance)
                if (!notificationManager.notificationChannels.contains(channel)) {
                    notificationManager.createNotificationChannel(channel)
                }
                NotificationCompat.Builder(context, channelId)
            } else {
                @Suppress("DEPRECATION")
                NotificationCompat.Builder(context)
            }
                .setContentTitle(effect.title)
                .setContentText(effect.text)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build()
            notificationManager to notification
        }.asFlow().flatMapConcat { (notificationManager, notification) ->
            if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(1, notification).toFlow().noActions()
            } else {
                showToastUC.flow("No permission to show notification")
            }
        }
    }

}
