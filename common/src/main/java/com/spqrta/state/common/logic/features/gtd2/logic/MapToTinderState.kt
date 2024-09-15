package com.spqrta.state.common.logic.features.gtd2.logic

import com.spqrta.state.common.logic.features.gtd2.TasksState
import com.spqrta.state.common.logic.features.gtd2.TinderTuple
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderPrompt
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderState

fun mapToTinderState(
    oldTinderTuple: TinderTuple,
    tasksState: TasksState,
): TinderState {
    val (oldTinderState, tasksDatabaseState) = oldTinderTuple
    val prompts = mutableListOf<TinderPrompt>()
    prompts.addAll(tasksState.nonEstimated().map {
        when (it) {
            is Task -> TinderPrompt.NonEstimatedTask(it)
            is Routine -> TinderPrompt.NonEstimatedRoutine(it)
            else -> throw IllegalStateException("Unexpected element type")
        }
    }.filter {
        !oldTinderState.skipped.contains(it)
    })
    return TinderState(prompts)
}
