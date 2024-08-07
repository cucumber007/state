package com.spqrta.state.common.logic.features.daily.timers

import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.TimerAction
import com.spqrta.state.common.logic.action.TimerAction.ProlongTimerAction
import com.spqrta.state.common.logic.action.TimerAction.StartTimer
import com.spqrta.state.common.logic.action.TimerAction.TimerEnded
import com.spqrta.state.common.logic.effect.ActionEffect
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.TickEffect
import com.spqrta.state.common.logic.features.daily.clock_mode.ClockMode
import com.spqrta.state.common.logic.features.daily.clock_mode.Update
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.optics.with
import com.spqrta.state.common.util.serialization.LocalDateTimeSerializer
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.time.Seconds
import com.spqrta.state.common.util.time.toMinutes
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.LocalDateTime

@Serializable
data class Timers(val timers: Map<TimerId, Timer> = mapOf()) {
    companion object {
        val reducer = widen(
            typeGet(),
            AppReadyOptics.optTimers with AppReadyOptics.optClockMode,
            Timers::reduce
        )

        private fun reduce(
            action: TimerAction,
            state: Pair<Map<TimerId, Timer>, ClockMode>
        ): Reduced<Pair<Map<TimerId, Timer>, ClockMode>, out AppEffect> {
            val oldTimers = state.first
            val oldClockMode = state.second
            return when (action) {
                is StartTimer -> {
                    val newClockMode = Update
                    val newTimers = (oldTimers + (action.id to Timer(
                        action.startDateTime,
                        action.id.duration
                    )))
                    (newTimers to newClockMode).withEffects(
                        TickEffect(Update.timeout)
                    )
                }

                is ClockAction.TickAction -> {
                    val (newTimers, endedTimers) = updateTimersAndGetEnded(action, oldTimers)
                    val newClockMode = if (newTimers.isEmpty()) {
                        ClockMode.INITIAL
                    } else {
                        oldClockMode
                    }
                    (newTimers to newClockMode).withEffects(
                        endedTimers.map { ActionEffect(TimerEnded(it)) }.toSet()
                    )
                }

                is TimerEnded -> {
                    (oldTimers.filter { it.key != action.timerId } to oldClockMode).withEffects()
                }

                is ProlongTimerAction -> {
                    state.withEffects()
                }
            }
        }

        private fun updateTimersAndGetEnded(
            action: ClockAction.TickAction,
            timers: Map<TimerId, Timer>
        ): Pair<Map<TimerId, Timer>, List<TimerId>> {
            val ended = mutableListOf<TimerId>()
            val newTimers = mutableMapOf<TimerId, Timer>()
            timers.keys.forEach { key ->
                timers[key]?.let { value ->
                    val left = key.duration.totalSeconds -
                            Duration.between(value.startedAt, action.time).toMillis() / 1000
                    value.copy(
                        left = left.toSeconds()
                    )
                }?.let {
                    newTimers[key] = it
                    if (it.left.seconds <= 0) {
                        ended.add(key)
                    }
                }
            }
            return (newTimers to ended)
        }
    }
}

@Serializable
sealed class TimerId(val duration: Seconds) {
    override fun toString(): String = javaClass.simpleName
}

@Serializable
object PromptTimer : TimerId(10.toSeconds())

@Serializable
object WorkTimer : TimerId(15.toMinutes())

@Serializable
data class Timer(
    @Serializable(with = LocalDateTimeSerializer::class)
    val startedAt: LocalDateTime,
    val left: Seconds
)
