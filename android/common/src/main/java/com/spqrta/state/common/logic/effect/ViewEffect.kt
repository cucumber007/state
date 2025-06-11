package com.spqrta.state.common.logic.effect

sealed interface ViewEffect : AppEffect {
    data class Scroll(
        val position: Int,
    ) : ViewEffect
}
