package com.spqrta.state.app.features.daily.timers

import com.spqrta.state.app.ActionEffect
import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.TickEffect
import com.spqrta.state.app.action.ClockAction
import com.spqrta.state.app.action.StateLoadedAction
import com.spqrta.state.app.action.TimerAction
import com.spqrta.state.app.action.TimerAction.*
import com.spqrta.state.app.features.daily.clock_mode.Update
import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.app.features.daily.clock_mode.ClockMode
import com.spqrta.state.app.features.daily.clock_mode.None
import com.spqrta.state.app.state.optics.AppReadyOptics
import com.spqrta.state.util.Seconds
import com.spqrta.state.util.optics.typeGet
import com.spqrta.state.util.optics.with
import com.spqrta.state.util.serialization.LocalDateTimeSerializer
import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.withEffects
import com.spqrta.state.util.toSeconds
import com.spqrta.state.util.optics.wrap
import com.spqrta.state.util.state_machine.widen
import com.spqrta.state.util.toMinutes
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

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
                        None
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
