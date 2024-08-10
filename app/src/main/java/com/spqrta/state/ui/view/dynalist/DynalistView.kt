package com.spqrta.state.ui.view.dynalist

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.dynalist.DynalistState
import com.spqrta.state.ui.view.gtd2.ElementView

@Composable
fun DynalistView(state: DynalistState) {
    Column {
        Text("Dynalist")
        when (state) {
            is DynalistState.Initial -> {
                Text("Initial")
            }

            is DynalistState.Loaded -> {
                state.elements.forEach {
                    ElementView(element = it)
                }
            }
        }
    }

}