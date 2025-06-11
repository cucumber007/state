package com.spqrta.state.common.ui.view.control

import com.spqrta.state.common.logic.action.AppAction


sealed class Control
data class Button(
    val text: String,
    val action: AppAction,
    val style: ButtonStyle = Ordinal
) : Control()
