package com.spqrta.state.common.logic.features.gtd2.element.routine

import android.content.Context
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.util.optics.OpticGet
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed class RoutineTrigger<Context : RoutineContext> {
    data class Day(val context: RoutineContext.Day) : RoutineTrigger<RoutineContext.Day>() {
        fun updateContextDelegate(context: RoutineContext.Day): Pair<RoutineTrigger<RoutineContext.Day>, Boolean> {
            val oldContext = this.context
            return copy(context = context).let {
                Pair(it, it.context.day != oldContext.day)
            }
        }

        override fun getContext(state: MetaState): RoutineContext.Day {
            return RoutineContext.Day(state.date)
        }
    }

    abstract fun getContext(state: MetaState): Context

    fun updateContext(metaState: MetaState): Pair<RoutineTrigger<Context>, Boolean> {
        return when (this) {
            is Day -> updateContextDelegate(getContext(metaState)).let {
                Pair(it.first as RoutineTrigger<Context>, it.second)
            }
        }
    }
}