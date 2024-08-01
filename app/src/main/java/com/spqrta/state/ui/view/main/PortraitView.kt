package com.spqrta.state.ui.view.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.ui.Portrait
import com.spqrta.state.ui.view.Header

// A wrapper around MainView to display it in portrait orientation
@Composable
fun PortraitView(state: AppReady) {
    Column(
        Modifier
            .padding(
                top = Dp(32f),
                bottom = Dp(16f),
                start = Dp(16f),
                end = Dp(16f),
            )
    ) {
        Header(state)
//        Text(
//            text = "State",
//            Modifier.align(Alignment.CenterHorizontally),
//            fontSize = TextUnit(20f, TextUnitType.Sp),
//            fontWeight = FontWeight.Bold
//        )
        MainView(state, Portrait)
    }
}