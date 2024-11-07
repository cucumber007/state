package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.dynalist.utility.pure.Optional
import com.spqrta.dynalist.utility.pure.toOptional
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.ToBeDone
import com.spqrta.state.common.util.optics.asOpticOptional
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class TimeredTask(
    val task: ToBeDone,
    val timeredState: TimeredState
) {
    val isTimerOverdue: Boolean by lazy {
        remainingTime.totalSeconds <= 0
    }

    val remainingTime: TimeValue by lazy {
        (task.estimate!!.totalSeconds - timeredState.timePassed.totalSeconds).toSeconds()
    }

    val passedTime: TimeValue by lazy {
        timeredState.timePassed
    }

    companion object {
        val optTimeredState = ({ state: Optional<TimeredTask> ->
            state.toNullable()?.timeredState
        } to { state: Optional<TimeredTask>, subState: TimeredState ->
            state.toNullable()?.copy(
                timeredState = subState
            ).toOptional()
        }).asOpticOptional()
    }
}
