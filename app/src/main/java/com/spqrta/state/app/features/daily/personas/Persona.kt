package com.spqrta.state.app.features.daily.personas

import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.PromptsEnabled
import com.spqrta.state.app.action.PersonaAction
import com.spqrta.state.app.action.PersonaAction.*
import com.spqrta.state.app.action.UndefinedPersonaAction
import com.spqrta.state.app.action.UndefinedPersonaAction.*
import com.spqrta.state.app.state.optics.AppReadyOptics
import com.spqrta.state.util.optics.typeGet
import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.withEffects
import com.spqrta.state.util.state_machine.plus
import com.spqrta.state.util.state_machine.widen
import kotlinx.serialization.Serializable

@Serializable
sealed class Persona {
    open val name: String = javaClass.simpleName
    override fun toString(): String = name

    companion object {
        val reducer = widen(
            typeGet(),
            AppReadyOptics.optPersona,
            Persona::reduce
        ) + widen(
            typeGet(),
            AppReadyOptics.optPersona,
            UndefinedPersona::reduce
        )

        fun reduce(action: PersonaAction, state: Persona): Reduced<out Persona, out AppEffect> {
            return when(action) {
                GetBackAction -> {
                    UndefinedPersona.withEffects()
                }
            }
        }

        const val I_AM_BACK = "I am back!"
    }
}

@Serializable
object UndefinedPersona : Persona() {
    fun reduce(
        action: UndefinedPersonaAction,
        state: Persona
    ): Reduced<out Persona, out AppEffect> {
        return when (action) {
            is DefinePersonaAction -> {
                action.persona.withEffects()
            }
        }
    }
}

@Serializable
data class Productive(
    val promptsEnabled: PromptsEnabled = PromptsEnabled
) : Persona()

@Serializable
object Depressed : Persona()

@Serializable
object Unstable : Persona()

@Serializable
object Irritated : Persona()
