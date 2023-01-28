package com.spqrta.state.app.state

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DailyState(
    val persona: PersonaCard,
    @Contextual
    val date: LocalDate = LocalDate.now()
) {
    companion object {
        val INITIAL = DailyState(UndefinedPersona)
    }
}
