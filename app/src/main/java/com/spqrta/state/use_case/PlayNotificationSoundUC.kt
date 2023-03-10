package com.spqrta.state.use_case

import android.content.Context
import android.media.RingtoneManager
import com.spqrta.state.app.action.AppAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Suppress("OPT_IN_USAGE")
class PlayNotificationSoundUC(
    private val context: Context
) {

    fun flow(): Flow<List<AppAction>> {
        return flow {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(context, notification)
            r.play()
            emit(listOf())
        }
    }
}
