package com.spqrta.state.app.action

import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.util.optics.OpticGet
import java.time.LocalDateTime

sealed class AppGlobalAction : AppAction {
    override fun toString(): String = javaClass.simpleName
}

data class AppErrorAction(val exception: Exception) : AppGlobalAction()
object InitAppAction : AppGlobalAction()
data class OnResumeAction(
    val datetime: LocalDateTime = LocalDateTime.now()
) : AppGlobalAction(), AppReadyAction
data class StateLoadedAction(
    val state: AppReady,
    val dateTime: LocalDateTime = LocalDateTime.now()
) : AppGlobalAction(), ClockAction
