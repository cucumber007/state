package com.spqrta.state.use_case

import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import com.spqrta.state.app.action.AppAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Suppress("OPT_IN_USAGE")
class VibrateUC(
    private val context: Context,
    private val playNotificationSoundUC: PlayNotificationSoundUC
) {

    fun flow(): Flow<List<AppAction>> {
        return flow {
            val vibrator = getSystemService(context, Vibrator::class.java)
            val canVibrate = vibrator?.hasVibrator() ?: false

            if (canVibrate) {
                val duration = 250
                val amplitude = 255
                val effect =
                    VibrationEffect.createOneShot(duration.toLong(), amplitude)
                vibrator?.vibrate(effect)
            } else {
                playNotificationSoundUC.flow()
            }
            emit(listOf())
        }
    }
}
