package com.spqrta.state

import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.effect.ViewEffect
import com.spqrta.state.common.util.toFlow
import kotlinx.coroutines.flow.Flow

object ViewEffectsHandler {
    suspend fun handle(effect: ViewEffect): Flow<List<AppAction>> {
        Effects.effects.emit(Effects.effects.value + effect)
        return listOf<AppAction>().toFlow()
    }
}
