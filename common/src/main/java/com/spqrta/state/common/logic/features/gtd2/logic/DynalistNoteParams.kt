package com.spqrta.state.common.logic.features.gtd2.logic

import com.spqrta.state.common.logic.features.gtd2.element.routine.RoutineContext
import com.spqrta.state.common.logic.features.gtd2.element.routine.RoutineTrigger
import com.spqrta.state.common.util.result.Res
import com.spqrta.state.common.util.result.tryRes
import com.spqrta.state.common.util.result.zip

data class DynalistNoteParams(
    val estimate: Int?,
    val trigger: RoutineTrigger<*>?,
) {
    companion object {
        const val KEY_ESTIMATE = "estimate"
        private const val KEY_CONTEXT = "trigger"

        fun parse(map: Map<String, String>): Res<DynalistNoteParams> {
            return zip(
                tryRes { map[KEY_ESTIMATE]?.toInt() },
                tryRes {
                    map[KEY_CONTEXT]?.let {
                        RoutineTrigger.fromString(it) ?: throw Exception("Invalid context: $it")
                    }
                }
            ).mapSuccess {
                DynalistNoteParams(it.first, it.second)
            }
        }
    }

}
