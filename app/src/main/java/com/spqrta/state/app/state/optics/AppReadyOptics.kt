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

    val optPersona = optDailyState.add(object : OpticOptional<DailyState, Persona> {
        override fun get(state: DailyState): Persona {
            return state.persona
        }

        override fun set(state: DailyState, subState: Persona): DailyState {
            return state.copy(persona = subState)
        }
    })

    val optPrompts = optDailyState.add(object : OpticOptional<DailyState, List<Prompt>> {
        override fun get(state: DailyState): List<Prompt> {
            return state.prompts
        }

        override fun set(state: DailyState, subState: List<Prompt>): DailyState {
            return state.copy(prompts = subState)
        }
    })

    private val optPromptsEnabled = optDailyState get { state: DailyState ->
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
}
