package com.spqrta.state.common.logic.features.gtd2.data

import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.util.time.toSeconds

object DebugQueue {
    val value = Routine(
        element = Queue(
            name = "Debug",
            elements = listOf(
                Task(
                    name = "5 seconds",
                    estimate = 5.toSeconds(),
                ),
            ),
        )
    )
}
