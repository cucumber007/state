package com.spqrta.state.common.logic.features.gtd2.logic

import com.spqrta.state.common.logic.features.gtd2.element.routine.RoutineContext
import com.spqrta.state.common.logic.features.gtd2.element.routine.RoutineTrigger
import com.spqrta.state.common.util.result.Res
import com.spqrta.state.common.util.result.tryRes
import com.spqrta.state.common.util.result.zip

data class DynalistNoteParams(
    val estimate: Int?,
    val flipper: Boolean,
    val schedule: String?,
    val trigger: RoutineTrigger<*>?,
) {
    companion object {
        const val KEY_ESTIMATE = "estimate"
        private const val KEY_CONTEXT = "trigger"
        private const val KEY_FLIPPER = "flipper"
        private const val KEY_SCHEDULE = "schedule"

        fun parse(map: Map<String, String>): Res<DynalistNoteParams> {
            return zip(
                tryRes { map[KEY_ESTIMATE]?.toInt() },
                tryRes {
                    map[KEY_CONTEXT]?.let {
                        RoutineTrigger.fromString(it) ?: throw Exception("Invalid context: $it")
                    }
                },
                tryRes { map[KEY_FLIPPER]?.toBoolean() ?: false }
            ).mapSuccess { (estimate, trigger, flipper) ->
                val schedule = map[KEY_SCHEDULE]
                DynalistNoteParams(
                    estimate = estimate,
                    flipper = flipper,
                    schedule = schedule,
                    trigger = trigger,
                )
            }
        }
    }

}
