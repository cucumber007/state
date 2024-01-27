package com.spqrta.state.common.use_case

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat.getSystemService
import com.spqrta.state.common.app.action.AppAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class VibrateUC(
    private val context: Context,
    private val playNotificationSoundUC: PlayNotificationSoundUC
) {

    fun flow(): Flow<List<AppAction>> {
        return getSystemService(context, Vibrator::class.java)
            ?.let { vibrator ->
                val canVibrate = vibrator.hasVibrator()
                if (canVibrate) vibrator else null
            }?.let { vibrator ->
                val duration = 250
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    {
                        val amplitude = 255
                        val effect = VibrationEffect.createOneShot(duration.toLong(), amplitude)
                        vibrator.vibrate(effect)
                        listOf<AppAction>()
                    }.asFlow()
                } else {
                    playNotificationSoundUC.flow()
                }
            } ?: playNotificationSoundUC.flow()
    }
}
