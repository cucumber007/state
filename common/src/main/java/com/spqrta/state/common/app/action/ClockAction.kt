package com.spqrta.state.common.app.action

import java.time.LocalDateTime

sealed interface ClockAction : AppAction {
    sealed class Action : ClockAction
    data class TickAction(val time: LocalDateTime = LocalDateTime.now()) : Action(),
        TimerAction
}
