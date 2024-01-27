package com.spqrta.state.common.app.features.daily

import com.spqrta.state.common.api.DateTime
import com.spqrta.state.common.app.Prompt
import com.spqrta.state.common.app.features.daily.clock_mode.ClockMode
import com.spqrta.state.common.app.features.daily.personas.Persona
import com.spqrta.state.common.app.features.daily.personas.Productive
import com.spqrta.state.common.app.features.daily.personas.UndefinedPersona
import com.spqrta.state.common.app.features.daily.personas.productive.ToDoList
import com.spqrta.state.common.app.features.daily.timers.Timers
import com.spqrta.state.common.util.optics.OpticGet
import com.spqrta.state.common.util.state_machine.plus
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDate

@Serializable
data class DailyState(
    val prompts: List<Prompt> = listOf(),
    val persona: Persona = UndefinedPersona,
    @Contextual
    val date: LocalDate = DateTime.dateNow
) {
    companion object {
        val reducer = ClockMode.reducer +
                Persona.reducer +
                Timers.reducer +
                Prompt.reducer

        val INITIAL = DailyState()
        val INITIAL_WATCH = DailyState(
            persona = Productive()
        )

        val optTodoList = object : OpticGet<DailyState, ToDoList> {
            override fun get(state: DailyState): ToDoList? {
                return when (state.persona) {
                    is Productive -> state.persona.toDoList
                    else -> null
                }
            }
        }
    }
}
