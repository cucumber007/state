package com.spqrta.state.app.state

import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.TickEffect
import com.spqrta.state.util.Seconds
import com.spqrta.state.util.state_machine.ReducerResult
import com.spqrta.state.util.state_machine.withEffects
import com.spqrta.state.util.toSeconds
import com.spqrta.state.util.wrap
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
sealed class ClockMode {
    override fun toString(): String = javaClass.simpleName

    sealed interface ClockAction: AppAction
    sealed class Action: ClockAction
    data class TickAction(val time: LocalDateTime = LocalDateTime.now()): Action(), Timers.TimerAction
    companion object {
        fun reduce(action: ClockAction, state: AppReady): ReducerResult<out AppReady, out AppEffect> {
            return when(action) {
                is TickAction -> {
                    wrap(state, AppReady.optClockMode) { clockMode ->
                        when(clockMode) {
                            None -> {
                                clockMode.withEffects()
                            }
                            is Second -> clockMode.withEffects(
                                TickEffect(clockMode.timeout)
                            )
                            is Update -> clockMode.withEffects(
                                TickEffect(clockMode.timeout)
                            )
                        }

                    }
                }
            }
        }
    }
}
object None: ClockMode()
object Second: ClockMode() {
    val timeout: Seconds = 1.toSeconds()
}
object Update: ClockMode() {
    val timeout: Seconds = 5.toSeconds()
}
