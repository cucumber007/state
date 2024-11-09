package com.spqrta.state.common.logic.features.gtd2.element.routine

import android.annotation.SuppressLint
import com.spqrta.state.common.environments.DateTimeEnvironment
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import kotlinx.serialization.Serializable

@SuppressLint("NewApi")
@Serializable
sealed class RoutineTrigger<Context : RoutineContext> {
    @Serializable
    data class Day(val context: RoutineContext.Day) :
        RoutineTrigger<RoutineContext.Day>() {
        @Suppress("KotlinConstantConditions")
        fun updateContextDelegate(
            context: RoutineContext.Day,
            routine: Routine<RoutineContext.Day>
        ): UpdateContextResult<RoutineContext.Day> {
            val oldContext = this.context
            return UpdateContextResult(
                newTrigger = copy(context = context),
                shouldReset = if (!routine.active) {
                    context.day != oldContext.day
                } else {
                    routine.active
                }
            )
        }

        override fun getContext(state: MetaState): RoutineContext.Day {
            return RoutineContext.Day(state.date)
        }

        companion object {
            val INITIAL = Day(RoutineContext.Day(DateTimeEnvironment.dateNow))
        }
    }

    abstract fun getContext(state: MetaState): Context

    @Suppress("UNCHECKED_CAST")
    fun <Context : RoutineContext> updateContext(
        metaState: MetaState,
        routine: Routine<Context>,
    ): UpdateContextResult<Context> {
        return when (this) {
            is Day -> {
                this.updateContextDelegate(
                    getContext(metaState),
                    routine.castContext { it as RoutineContext.Day }
                ) as UpdateContextResult<Context>
            }
        }
    }


    companion object {
        fun fromString(data: String): RoutineTrigger<*>? {
            return when (data) {
                Day::class.simpleName -> {
                    RoutineTrigger.Day.INITIAL
                }

                else -> null
            }
        }
    }
}
