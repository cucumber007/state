package com.spqrta.state.ui.view.gtd2.element

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.element.Routine

@Composable
fun RoutineView(routine: Routine<*>, displayName: String? = null) {
    Column {
        ElementView(
            element = routine.innerElement,
            displayName = displayName ?: routine.displayName,
        )
    }
}