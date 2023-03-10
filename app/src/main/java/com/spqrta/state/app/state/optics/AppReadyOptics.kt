package com.spqrta.state.app.state.optics

import com.spqrta.state.app.*
import com.spqrta.state.app.features.daily.clock_mode.ClockMode
import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.app.features.daily.DailyState
import com.spqrta.state.app.features.daily.personas.*
import com.spqrta.state.app.features.daily.timers.Timer
import com.spqrta.state.app.features.daily.timers.TimerId
import com.spqrta.state.app.features.daily.timers.Timers
import com.spqrta.state.app.features.stats.Stats
import com.spqrta.state.app.features.storage.Storage
import com.spqrta.state.app.state.optics.AppReadyOptics.optDailyState
import com.spqrta.state.app.state.optics.AppReadyOptics.optPrompts
import com.spqrta.state.util.optics.*

object AppReadyOptics {
    val optTimers = object : OpticOptional<AppReady, Map<TimerId, Timer>> {
        override fun get(state: AppReady): Map<TimerId, Timer> {
            return state.timers.timers
        }

        override fun set(state: AppReady, subState: Map<TimerId, Timer>): AppReady {
            return state.copy(timers = Timers(subState))
        }
    }
    val optStats = object : OpticOptional<AppReady, Stats> {
        override fun get(state: AppReady): Stats {
            return state.stats
        }

        override fun set(state: AppReady, subState: Stats): AppReady {
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
        override fun get(state: AppReady): DailyState {
            return state.dailyState
        }

        override fun set(state: AppReady, subState: DailyState): AppReady {
            return state.copy(dailyState = subState)
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
        when(val persona = state.persona) {
            is Productive -> persona.promptsEnabled
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
