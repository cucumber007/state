package com.spqrta.state.ui.view.gtd2

import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Flipper
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task

@Composable
fun ElementView(element: Element) {
    when (element) {
        is Queue -> QueueView(queue = element)
        is Routine -> RoutineView(routine = element)
        is Task -> TaskView(task = element)
        is Flipper -> FlipperView(flipper = element)
    }
}