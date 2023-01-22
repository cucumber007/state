package com.spqrta.state.app

import com.spqrta.state.app.state.AppReady

sealed class AppEffect {
    override fun toString(): String = javaClass.simpleName
}
data class SaveStateEffect(val state: AppReady): AppEffect()
object LoadStateEffect: AppEffect()
