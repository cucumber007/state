package com.spqrta.state.common.logic.features.global

import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.Prompt
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.util.Seconds

sealed class AppEffect {
    override fun toString(): String = javaClass.simpleName
}

data class SaveStateEffect(val state: AppReady) : AppEffect()
object LoadStateEffect : AppEffect()
data class TickEffect(val duration: Seconds) : AppEffect()
data class ActionEffect(val action: AppAction) : AppEffect()
data class AddPromptEffect(val prompt: Prompt) : AppEffect()
object PlayNotificationSoundEffect : AppEffect()
object VibrateEffect : AppEffect()
