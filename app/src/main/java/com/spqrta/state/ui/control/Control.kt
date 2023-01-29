package com.spqrta.state.ui.control

import com.spqrta.state.app.action.AppAction

sealed class Control
data class Button(
    val text: String,
    val action: AppAction,
    val style: ButtonStyle = Ordinal
): Control()
