package com.spqrta.state.ui.view.gtd2

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.element.Routine

@Composable
fun RoutineView(routine: Routine) {
    Column {
        ElementView(
            element = routine.normalizedTask
        )
    }
}