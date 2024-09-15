package com.spqrta.state.common.logic.action

import com.spqrta.state.common.environments.DateTimeEnvironment
import com.spqrta.state.common.logic.AppReady
import java.time.LocalDateTime

/**
 * Service actions for the whole app
 */
sealed class AppGlobalAction : AppAction {
    override fun toString(): String = javaClass.simpleName

    object OnDebugMenuButtonClick : AppGlobalAction()
}

data class AppErrorAction(val exception: Exception) : AppGlobalAction()
object InitAppAction : AppGlobalAction()

data class OnResumeAction(
    val datetime: LocalDateTime = DateTimeEnvironment.dateTimeNow
) : AppGlobalAction(), AppReadyAction

data class StateLoadedAction(
    val state: AppReady,
    val dateTime: LocalDateTime = DateTimeEnvironment.dateTimeNow
) : AppGlobalAction(), ClockAction, Gtd2Action, DynalistAction
