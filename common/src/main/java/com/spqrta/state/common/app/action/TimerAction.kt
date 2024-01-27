package com.spqrta.state.common.app.action

import com.spqrta.state.common.app.features.daily.timers.TimerId
import com.spqrta.state.common.util.TimeValue
import java.time.LocalDateTime

sealed interface TimerAction : AppAction {
    sealed class Action : TimerAction
    data class StartTimer(
        val id: TimerId,
        val startDateTime: LocalDateTime = LocalDateTime.now()
    ) : Action()

    data class TimerEnded(
        val timerId: TimerId
    ) : Action(), PromptAction, ProductiveActivityAction

    data class ProlongTimerAction(val timerId: TimerId, val amount: TimeValue) : Action()
}
