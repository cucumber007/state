package com.spqrta.state.ui.view.gtd2

import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.Element
import com.spqrta.state.common.logic.features.gtd2.Queue
import com.spqrta.state.common.logic.features.gtd2.Routine
import com.spqrta.state.common.logic.features.gtd2.Task

@Composable
fun ElementView(element: Element) {
    when (element) {
        is Queue -> QueueView(queue = element)
        is Routine -> RoutineView(routine = element)
        is Task -> TaskView(task = element)
    }
}