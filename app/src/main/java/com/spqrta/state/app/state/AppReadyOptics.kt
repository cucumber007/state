package com.spqrta.state.app.state

import com.spqrta.state.util.OpticOptional

object AppReadyOptics {
    val optPersona = object: OpticOptional<AppReady, PersonaCard> {
        override fun get(state: AppReady): PersonaCard {
            return state.persona
        }

        override fun set(state: AppReady, subState: PersonaCard): AppReady {
            return state.copy(persona = subState)
        }
    }
}
