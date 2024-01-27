package com.spqrta.state.common.app.action

import com.spqrta.state.common.api.DateTime
import com.spqrta.state.common.app.features.core.AppReady
import com.spqrta.state.common.app.features.daily.DailyState
import org.threeten.bp.LocalDateTime

sealed class AppGlobalAction : AppAction {
    override fun toString(): String = javaClass.simpleName
}

data class AppErrorAction(val exception: Exception) : AppGlobalAction()
data class InitAppAction(val defaultState: AppReady) : AppGlobalAction()
data class OnResumeAction(
    val datetime: LocalDateTime = DateTime.dateTimeNow,
    val defaultDailyState: DailyState
) : AppGlobalAction(), AppReadyAction

data class StateLoadedAction(
    val state: AppReady,
    val dateTime: LocalDateTime = DateTime.dateTimeNow,
    val defaultDailyState: DailyState
) : AppGlobalAction(), ClockAction
