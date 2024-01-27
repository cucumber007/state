package com.spqrta.state.common.app.action

import org.threeten.bp.LocalDateTime


sealed interface ClockAction : AppAction {
    sealed class Action : ClockAction
    data class TickAction(val time: LocalDateTime = LocalDateTime.now()) : Action(),
        TimerAction
}
