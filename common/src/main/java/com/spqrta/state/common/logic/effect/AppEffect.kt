package com.spqrta.state.common.logic.effect

import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.Prompt
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.util.time.Seconds

sealed interface AppEffect
data class ActionEffect(val action: AppAction) : AppEffect
data class AddPromptEffect(val prompt: Prompt) : AppEffect
data class SaveStateEffect(val state: AppReady) : AppEffect
data class ShowToastEffect(val message: String) : AppEffect
data class TickEffect(val duration: Seconds) : AppEffect
object UpdateStatsEffect : AppEffect {
    override fun toString(): String = javaClass.simpleName
}

object LoadStateEffect : AppEffect {
    override fun toString(): String = javaClass.simpleName
}

object PlayNotificationSoundEffect : AppEffect {
    override fun toString(): String = javaClass.simpleName
}

object VibrateEffect : AppEffect {
    override fun toString(): String = javaClass.simpleName
}
