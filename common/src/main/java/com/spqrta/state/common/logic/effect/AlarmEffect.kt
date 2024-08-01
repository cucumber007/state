package com.spqrta.state.common.logic.effect

sealed interface AlarmEffect : AppEffectInterface {
    sealed class Effect : AlarmEffect {
        override fun toString(): String = javaClass.simpleName
    }

}