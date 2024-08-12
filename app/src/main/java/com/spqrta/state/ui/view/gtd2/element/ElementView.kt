package com.spqrta.state.ui.view.gtd2.element

import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Flipper
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task

@Composable
fun ElementView(element: Element, displayName: String? = null) {
    when (element) {
        is Queue -> {
            QueueView(
                queue = element,
                displayName = displayName,
            )
        }

        is Routine -> {
            RoutineView(
                routine = element,
                displayName = displayName,
            )
        }

        is Task -> {
            TaskView(
                task = element,
                displayName = displayName,
            )
        }

        is Flipper -> {
            FlipperView(
                flipper = element,
                displayName = displayName,
            )
        }
    }
}