package com.spqrta.state.app.features.daily

import com.spqrta.state.app.Prompt
import com.spqrta.state.app.action.PersonaAction
import com.spqrta.state.app.features.daily.clock_mode.ClockMode
import com.spqrta.state.app.features.daily.personas.Persona
import com.spqrta.state.app.features.daily.personas.UndefinedPersona
import com.spqrta.state.app.features.daily.timers.Timers
import com.spqrta.state.util.state_machine.plus
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DailyState(
    val prompts: List<Prompt> = listOf(),
    val persona: Persona = UndefinedPersona,
    @Contextual
    val date: LocalDate = LocalDate.now()
) {
    companion object {
        val reducer = ClockMode.reducer +
                Persona.reducer +
                Timers.reducer +
                Prompt.reducer
        
        val INITIAL = DailyState()
    }
}
