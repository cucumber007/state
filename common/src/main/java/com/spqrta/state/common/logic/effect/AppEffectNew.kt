package com.spqrta.state.common.logic.effect

sealed interface AppEffectNew : AppEffect {
    object StartFgs : AppEffectNew {
        override fun toString(): String = javaClass.simpleName
    }
}
