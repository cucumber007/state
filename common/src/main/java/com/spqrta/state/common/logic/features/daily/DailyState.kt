package com.spqrta.state.common.logic.features.daily

import com.spqrta.state.common.logic.Prompt
import com.spqrta.state.common.logic.features.daily.clock_mode.ClockMode
import com.spqrta.state.common.logic.features.daily.personas.Persona
import com.spqrta.state.common.logic.features.daily.personas.UndefinedPersona
import com.spqrta.state.common.logic.features.daily.timers.Timers
import com.spqrta.state.common.environments.DateTimeEnvironment
import com.spqrta.state.common.util.state_machine.plus
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DailyState(
    val prompts: List<Prompt> = listOf(),
    val persona: Persona = UndefinedPersona,
    @Contextual
    val date: LocalDate = DateTimeEnvironment.dateNow
) {
    companion object {
        val reducer = ClockMode.reducer +
                Persona.reducer +
                Timers.reducer +
                Prompt.reducer

        val INITIAL = DailyState()
    }
}
