package com.spqrta.state.common.logic.features.global

import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.Prompt
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.util.Seconds

sealed class AppEffectLegacy {
    override fun toString(): String = javaClass.simpleName
}

data class SaveStateEffect(val state: AppReady) : AppEffectLegacy()
object LoadStateEffect : AppEffectLegacy()
data class TickEffect(val duration: Seconds) : AppEffectLegacy()
data class ActionEffect(val action: AppAction) : AppEffectLegacy()
data class AddPromptEffect(val prompt: Prompt) : AppEffectLegacy()
object PlayNotificationSoundEffect : AppEffectLegacy()
object VibrateEffect : AppEffectLegacy()
data class AppEffectNew(val effect: AppEffect) : AppEffectLegacy()
