package com.spqrta.state.app.state.optics

import com.spqrta.state.app.state.*
import com.spqrta.state.util.OpticOptional
import com.spqrta.state.util.plus

object AppReadyOptics {
    val optTimers = object: OpticOptional<AppReady, Map<TimerId, Timer>> {
        override fun get(state: AppReady): Map<TimerId, Timer> {
            return state.timers.timers
        }

        override fun set(state: AppReady, subState: Map<TimerId, Timer>): AppReady {
            return state.copy(timers = Timers(subState))
        }
    }
    val optStats = object: OpticOptional<AppReady, Stats> {
        override fun get(state: AppReady): Stats {
            return state.stats
        }

        override fun set(state: AppReady, subState: Stats): AppReady {
            return state.copy(stats = subState)
        }
    }

    val optClockMode = object: OpticOptional<AppReady, ClockMode> {
        override fun get(state: AppReady): ClockMode {
            return state.clockMode
        }

        override fun set(state: AppReady, subState: ClockMode): AppReady {
            return state.copy(clockMode = subState)
        }
    }

    val optDailyState = object: OpticOptional<AppReady, DailyState> {
        override fun get(state: AppReady): DailyState {
            return state.dailyState
        }

        override fun set(state: AppReady, subState: DailyState): AppReady {
            return state.copy(dailyState = subState)
        }
    }

    val optPersona = optDailyState.plus(object: OpticOptional<DailyState, PersonaCard> {
        override fun get(state: DailyState): PersonaCard {
            return state.persona
        }

        override fun set(state: DailyState, subState: PersonaCard): DailyState {
            return state.copy(persona = subState)
        }
    })
}
