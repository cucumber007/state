package com.spqrta.state.ui.view.tinder

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderState

@Composable
fun TinderView(state: TinderState) {
    Text(state.toString())
}