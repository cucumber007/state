package com.spqrta.state.app

import com.spqrta.state.app.action.AppAction
import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.app.features.daily.timers.TimerId
import com.spqrta.state.util.Seconds
import com.spqrta.state.util.TimeValue

sealed class AppEffect {
    override fun toString(): String = javaClass.simpleName
}
data class SaveStateEffect(val state: AppReady): AppEffect()
object LoadStateEffect: AppEffect()
data class TickEffect(val duration: Seconds): AppEffect()
data class ActionEffect(val action: AppAction): AppEffect()
data class AddPromptEffect(val prompt: Prompt): AppEffect()
object PlayNotificationSoundEffect: AppEffect()
