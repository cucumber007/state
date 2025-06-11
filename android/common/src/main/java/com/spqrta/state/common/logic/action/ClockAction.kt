package com.spqrta.state.common.logic.action

import android.annotation.SuppressLint
import com.spqrta.state.common.environments.DateTimeEnvironment
import java.time.LocalDateTime

@SuppressLint("NewApi")
sealed interface ClockAction : AppAction {
    sealed class Action : ClockAction
    data class TickAction(val time: LocalDateTime = DateTimeEnvironment.dateTimeNow) : Action(),
        TimerAction, AlarmAction, StatsAction, DynalistAction, CurrentAction, Gtd2Action
}
