package com.spqrta.state.app.state

import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.TickEffect
import com.spqrta.state.util.Seconds
import com.spqrta.state.util.serialization.LocalDateTimeSerializer
import com.spqrta.state.util.state_machine.ReducerResult
import com.spqrta.state.util.state_machine.withEffects
import com.spqrta.state.util.toSeconds
import com.spqrta.state.util.wrap
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.LocalDateTime
import kotlinx.serialization.Transient

@Serializable
data class Timers(val timers: Map<TimerId, Timer> = mapOf()) {
    sealed interface TimerAction : AppAction
    sealed class Action : TimerAction
    data class StartTimer(
        val id: TimerId,
        val startDateTime: LocalDateTime = LocalDateTime.now()
    ) : Action()

    companion object {
        fun reduce(
            action: TimerAction,
            state: AppReady
        ): ReducerResult<out AppReady, out AppEffect> {
            return wrap(state, AppReady.optTimers, AppReady.optClockMode) { timers, oldClockMode ->
                when (action) {
                    is StartTimer -> {
                        val newClockMode = Update
                        val newTimers = (timers + (action.id to Timer(
                            action.startDateTime,
                            action.id.duration
                        )))
                        (newTimers to newClockMode).withEffects(
                            TickEffect(newClockMode.timeout)
                        )
                    }
                    is ClockMode.TickAction -> {
                        val newTimers = mutableMapOf<TimerId, Timer>()
                        timers.keys.forEach { key ->
                            timers[key]?.let { value ->
                                val left = key.duration.totalSeconds -
                                        Duration.between(value.startedAt, action.time).toMillis() / 1000
                                value.copy(
                                    left = left.toSeconds()
                                )
                            }?.let {
                                if(it.left.seconds > 0) {
                                    newTimers[key] = it
                                }
                            }
                        }
                        val newClockMode = if(newTimers.isEmpty()) {
                            Update
                        } else {
                            oldClockMode
                        }
                        (newTimers to newClockMode).withEffects()
                    }
                }
            }
        }
    }
}

@Serializable
sealed class TimerId(val duration: Seconds) {
    override fun toString(): String = javaClass.simpleName
}

@Serializable
object TestTimer : TimerId(10.toSeconds())

@Serializable
data class Timer(
    @Serializable(with = LocalDateTimeSerializer::class)
    val startedAt: LocalDateTime,
    val left: Seconds
)
