package com.spqrta.state.ui.control

import com.spqrta.state.app.state.AppAction

sealed class Control
data class Button(
    val text: String,
    val action: AppAction,
    val style: ButtonStyle = Ordinal
): Control()
