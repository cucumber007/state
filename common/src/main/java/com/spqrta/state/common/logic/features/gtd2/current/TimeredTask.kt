package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.util.optics.asOpticOptional
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class TimeredTask(
    val task: Task,
    val timeredState: TimeredState
) {
    val remainingTime: TimeValue by lazy {
        (task.estimate()!!.totalSeconds - timeredState.timePassed.totalSeconds).toSeconds()
    }

    val passedTime: TimeValue by lazy {
        timeredState.timePassed
    }

    companion object {
        val optTimeredState = ({ state: TimeredTask ->
            state.timeredState
        } to { state: TimeredTask, subState: TimeredState ->
            state.copy(
                timeredState = subState
            )
        }).asOpticOptional()
    }
}
