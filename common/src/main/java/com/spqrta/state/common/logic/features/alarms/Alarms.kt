package com.spqrta.state.common.logic.features.alarms

import com.spqrta.state.common.logic.action.AlarmAction
import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import java.time.LocalTime

object Alarms {

    val reducer = widen(
        typeGet(),
        AppStateOptics.optAlarmsState,
        Alarms::reduce,
    )

    fun reduce(action: AlarmAction, state: AlarmsState): Reduced<AlarmsState, AppEffect> {
        return when (action) {
            is AlarmAction.CreateAlarm -> {
                val newId = if (state.alarms.isNotEmpty()) {
                    state.alarms.maxBy { it.id }.id + 1
                } else {
                    0
                }
                state.copy(alarms = state.alarms + Alarm(newId, action.time)).withEffects()
            }

            is AlarmAction.DeleteAlarm -> {
                state.copy(alarms = state.alarms.filter { it.id != action.id }).withEffects()
            }

            is ClockAction.TickAction -> {
                val firedAlarms = state.alarms.filter { it.time <= action.time.toLocalTime() }
                state.copy(
                    currentTime = LocalTime.now(),
                    alarms = state.alarms.filter { !firedAlarms.contains(it) }
                ).withEffects()
            }
        }
    }
}