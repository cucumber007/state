package com.spqrta.state.app.state.optics

import com.spqrta.state.app.state.AppReady
import com.spqrta.state.app.state.DailyState
import com.spqrta.state.app.state.PersonaCard
import com.spqrta.state.app.state.Stats
import com.spqrta.state.util.OpticOptional
import com.spqrta.state.util.plus

object AppReadyOptics {
    val optStats = object: OpticOptional<AppReady, Stats> {
        override fun get(state: AppReady): Stats {
            return state.stats
        }

        override fun set(state: AppReady, subState: Stats): AppReady {
            return state.copy(stats = subState)
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
