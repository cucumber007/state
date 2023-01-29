package com.spqrta.state.app

import com.spqrta.state.app.state.AppAction
import com.spqrta.state.app.state.AppReady
import com.spqrta.state.util.Seconds

sealed class AppEffect {
    override fun toString(): String = javaClass.simpleName
}
data class SaveStateEffect(val state: AppReady): AppEffect()
object LoadStateEffect: AppEffect()
data class TickEffect(val duration: Seconds): AppEffect()
data class ActionEffect(val action: AppAction): AppEffect()
