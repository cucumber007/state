package com.spqrta.state.common.logic.features.gtd2.tinder

import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task
import kotlinx.serialization.Serializable

@Serializable
sealed class TinderPrompt {

    @Serializable
    data class NonEstimatedRoutine(val routine: Routine) : TinderPrompt()

    @Serializable
    data class NonEstimatedTask(val task: Task) : TinderPrompt()

}