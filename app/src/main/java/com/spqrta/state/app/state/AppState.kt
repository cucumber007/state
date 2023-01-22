package com.spqrta.state.app.state

import kotlinx.serialization.Serializable

sealed class AppState

object AppNotInitialized : AppState()

@Serializable
data class AppReady(
    val persona: PersonaCard
) : AppState() {
    companion object {
        val INITIAL = AppReady(UndefinedPersona)
    }
}

