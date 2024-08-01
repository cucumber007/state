package com.spqrta.state.common.logic.features.daily.personas

import com.spqrta.state.common.logic.action.PersonaAction
import com.spqrta.state.common.logic.action.PersonaAction.GetBackAction
import com.spqrta.state.common.logic.action.UndefinedPersonaAction
import com.spqrta.state.common.logic.action.UndefinedPersonaAction.DefinePersonaAction
import com.spqrta.state.common.logic.features.daily.personas.productive.Flipper
import com.spqrta.state.common.logic.features.daily.personas.productive.Navigation
import com.spqrta.state.common.logic.features.daily.personas.productive.ToDoList
import com.spqrta.state.common.logic.features.global.AppEffect
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.util.optics.asOpticOptional
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.plus
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
sealed class Persona {
    open val name: String = javaClass.simpleName
    override fun toString(): String = name

    companion object {
        val optProductive = ({ state: Persona ->
            if (state is Productive) state else null
        } to { state: Persona, subState: Productive ->
            subState as Persona
        }).asOpticOptional()

        val reducer = widen(
            typeGet(),
            AppReadyOptics.optPersona,
            Persona::reduce
        ) + widen(
            typeGet(),
            AppReadyOptics.optPersona,
            UndefinedPersona::reduce
        ) + widen(
            typeGet(),
            AppReadyOptics.optPersona + optProductive + Productive.optActivity,
            Productive::reduce
        ) + widen(
            typeGet(),
            AppReadyOptics.optPersona + optProductive + Productive.optFlipper,
            Flipper::reduce
        ) + widen(
            typeGet(),
            AppReadyOptics.optPersona + optProductive + Productive.optToDoList,
            ToDoList::reduce
        ) + widen(
            typeGet(),
            AppReadyOptics.optPersona + optProductive + Productive.optNavigation,
            Navigation::reduce
        )

        fun reduce(action: PersonaAction, state: Persona): Reduced<out Persona, out AppEffect> {
            return when (action) {
                GetBackAction -> {
                    UndefinedPersona.withEffects()
                }
            }
        }

        const val I_AM_BACK = "I am back!"
        const val I_AM_NOT_GOOD = "I am not good"
    }
}

@kotlinx.serialization.Serializable
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

@kotlinx.serialization.Serializable
object Depressed : Persona()

@kotlinx.serialization.Serializable
object Unstable : Persona()

@Serializable
object Irritated : Persona()
