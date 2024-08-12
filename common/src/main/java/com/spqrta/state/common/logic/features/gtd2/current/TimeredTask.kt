package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.util.optics.asOpticOptional
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class TimeredTask(
    val task: Task,
    val timerState: TimeredState
) {
    val remainingTime: TimeValue by lazy {
        (task.estimate()!!.totalSeconds - timerState.timePassed.totalSeconds).toSeconds()
    }

    companion object {
        val optTimeredState = ({ state: TimeredTask ->
            state.timerState
        } to { state: TimeredTask, subState: TimeredState ->
            state.copy(
                timerState = subState
            )
        }).asOpticOptional()
    }
}