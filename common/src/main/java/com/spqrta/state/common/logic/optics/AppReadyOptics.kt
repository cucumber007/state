package com.spqrta.state.common.logic.optics

import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.Prompt
import com.spqrta.state.common.logic.features.daily.DailyState
import com.spqrta.state.common.logic.features.daily.clock_mode.ClockMode
import com.spqrta.state.common.logic.features.daily.personas.Depressed
import com.spqrta.state.common.logic.features.daily.personas.Irritated
import com.spqrta.state.common.logic.features.daily.personas.Persona
import com.spqrta.state.common.logic.features.daily.personas.Productive
import com.spqrta.state.common.logic.features.daily.personas.UndefinedPersona
import com.spqrta.state.common.logic.features.daily.personas.Unstable
import com.spqrta.state.common.logic.features.daily.timers.Timer
import com.spqrta.state.common.logic.features.daily.timers.TimerId
import com.spqrta.state.common.logic.features.daily.timers.Timers
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.stats.StatsState
import com.spqrta.state.common.logic.features.storage.Storage
import com.spqrta.state.common.util.optics.Optic
import com.spqrta.state.common.util.optics.OpticOptional
import com.spqrta.state.common.util.optics.asOptic
import com.spqrta.state.common.util.optics.asOpticGet
import com.spqrta.state.common.util.optics.gather
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.withGet

object AppReadyOptics {
    val optGtd2State: OpticOptional<AppReady, Gtd2State> =
        ({ state: AppReady ->
            state.gtd2State
        } to { state: AppReady, subState: Gtd2State ->
            state.copy(
                gtd2State = subState
            )
        }).asOptic()


    val optTimers = object : OpticOptional<AppReady, Map<TimerId, Timer>> {
        override fun get(state: AppReady): Map<TimerId, Timer> {
            return state.timers.timers
        }

        override fun set(state: AppReady, subState: Map<TimerId, Timer>): AppReady {
            return state.copy(timers = Timers(subState))
        }
    }
    val optStats = object : OpticOptional<AppReady, StatsState> {
        override fun get(state: AppReady): StatsState {
            return state.stats
        }

        override fun set(state: AppReady, subState: StatsState): AppReady {
            return state.copy(stats = subState)
        }
    }
    val optClockMode = object : OpticOptional<AppReady, ClockMode> {
        override fun get(state: AppReady): ClockMode {
            return state.clockMode
        }

        override fun set(state: AppReady, subState: ClockMode): AppReady {
            return state.copy(clockMode = subState)
        }
    }

    val optDailyState = object : OpticOptional<AppReady, DailyState> {
        override fun get(state: AppReady): DailyState? {
            return null
        }

        override fun set(state: AppReady, subState: DailyState): AppReady {
            return state
        }
    }

    val optPersona = optDailyState + ({ state: DailyState ->
        state.persona
    } to { state: DailyState, subState: Persona ->
        state.copy(persona = subState)
    }).asOptic()

    val optPrompts = optDailyState + ({ state: DailyState ->
        state.prompts
    } to { state: DailyState, subState: List<Prompt> ->
        state.copy(prompts = subState)
    }).asOptic()

    private val optPromptsEnabled = optDailyState withGet { state: DailyState ->
        when (val persona = state.persona) {
//            is Productive -> persona.promptsEnabled
            is Productive -> null
            Depressed,
            Irritated,
            UndefinedPersona,
            Unstable -> null
        }
    }.asOpticGet()

    val optActivePrompt = gather(
        optPrompts,
        optPromptsEnabled
    ) { prompts, _ ->
        prompts.minByOrNull { it.priority.toString() + it.javaClass.simpleName }
    }

    val optStorage = object : Optic<AppReady, Storage> {
        override fun getStrict(state: AppReady): Storage {
            return state.storage
        }

        override fun set(state: AppReady, subState: Storage): AppReady {
            return state.copy(storage = subState)
        }
    }

    val optIsStorageOk = optStorage withGet Storage.optIsOk

}
