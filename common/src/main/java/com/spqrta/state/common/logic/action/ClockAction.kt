package com.spqrta.state.common.logic.action

import android.annotation.SuppressLint
import java.time.LocalDateTime

@SuppressLint("NewApi")
sealed interface ClockAction : AppAction {
    sealed class Action : ClockAction
    data class TickAction(val time: LocalDateTime = LocalDateTime.now()) : Action(),
        TimerAction, AlarmAction, StatsAction, DynalistAction, CurrentAction, Gtd2Action
}
