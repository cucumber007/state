package com.spqrta.state.ui.view.main.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.ui.Portrait
import com.spqrta.state.ui.view.main.MainView

@Preview
@Composable
fun Preview() {
    MainView(AppReady.INITIAL, Portrait)
}