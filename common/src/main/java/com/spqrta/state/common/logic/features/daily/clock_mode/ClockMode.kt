package com.spqrta.state.common.logic.features.daily.clock_mode

import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.ClockAction.TickAction
import com.spqrta.state.common.logic.action.StateLoadedAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.TickEffect
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.time.Seconds
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@Serializable
sealed class ClockMode {
    override fun toString(): String = javaClass.simpleName

    companion object {
        val INITIAL: ClockMode = None

        val reducer = widen(
            typeGet(),
            AppReadyOptics.optClockMode,
            Companion::reduce
        )

        @Suppress("UnnecessaryVariable")
        private fun reduce(
            action: ClockAction,
            clockMode: ClockMode
        ): Reduced<out ClockMode, out AppEffect> {
            return when (action) {
                is TickAction -> {
                    when (clockMode) {
                        None -> {
                            clockMode.withEffects()
                        }

                        is Second -> {
                            clockMode.withEffects(
                                TickEffect(Second.timeout)
                            )
                        }

                        is Update -> {
                            clockMode.withEffects(
                                TickEffect(Update.timeout)
                            )
                        }
                    }
                }

                is StateLoadedAction -> {
                    reduce(TickAction(action.dateTime), clockMode)
                }
            }
        }
    }
}

@Serializable
object None : ClockMode()

@Serializable
object Second : ClockMode() {
    val timeout: Seconds = 1.toSeconds()
}

@Serializable
object Update : ClockMode() {
    val timeout: Seconds = 5.toSeconds()
}
