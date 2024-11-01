package com.spqrta.state.common.use_case.foreground_service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.AppReadyAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class StartFgsUC(
    private val context: Context,
) {
    fun flow(): Flow<List<AppAction>> {
        return {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(StateService.createStartIntent(context))
                }
                listOf()
            } else {
                listOf(AppReadyAction.ShowErrorAction(Exception("Notifications permission is not granted")))
            }
        }.asFlow()
    }
}
