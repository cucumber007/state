package com.spqrta.state.app.features.daily.clock_mode

import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.TickEffect
import com.spqrta.state.app.action.ClockAction
import com.spqrta.state.app.action.ClockAction.*
import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.app.state.optics.AppReadyOptics
import com.spqrta.state.app.state.optics.AppStateOptics
import com.spqrta.state.util.Seconds
import com.spqrta.state.util.optics.plus
import com.spqrta.state.util.optics.typeGet
import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.withEffects
import com.spqrta.state.util.toSeconds
import com.spqrta.state.util.state_machine.widen
import kotlinx.serialization.Serializable

@Serializable
sealed class ClockMode {
    override fun toString(): String = javaClass.simpleName

    companion object {
        val reducer = widen(
            typeGet(),
            AppReadyOptics.optClockMode,
            Companion::reduce
        )

        @Suppress("UnnecessaryVariable")
        fun reduce(action: ClockAction, clockMode: ClockMode): Reduced<out ClockMode, out AppEffect> {
            return when(action) {
                is TickAction -> {
                    when(clockMode) {
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
