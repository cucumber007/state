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
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

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
