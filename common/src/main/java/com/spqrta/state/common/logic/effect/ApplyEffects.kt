package com.spqrta.state.common.logic.effect

import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.PromptAction
import com.spqrta.state.common.use_case.UseCases
import com.spqrta.state.common.util.collections.asList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

fun applyEffects(
    effects: Set<AppEffect>,
    effectsScope: CoroutineScope,
    useCases: UseCases,
    handleAction: (AppAction) -> Unit
) {
    effects.forEach { effect ->
        effectsScope.launch {
            with(useCases) {
                when (effect) {
                    is ActionEffect -> {
                        { effect.action.asList() }.asFlow()
                    }

                    is AddPromptEffect -> {
                        { PromptAction.AddPrompt(effect.prompt).asList() }.asFlow()
                    }

                    is LoadDynalistEffect -> {
                        loadDynalistUC.flow()
                    }

                    is LoadStateEffect -> {
                        loadStateUC.flow()
                    }

                    is PlayNotificationSoundEffect -> {
                        playNotificationSoundUC.flow()
                    }

                    is SaveStateEffect -> {
                        saveStateUC.flow(effect.state)
                    }

                    is ShowToastEffect -> {
                        showToastUC.flow(effect.message)
                    }

                    is TickEffect -> {
                        tickUC.flow(effect.duration)
                    }

                    is VibrateEffect -> {
                        vibrateUC.flow()
                    }

                    is UpdateStatsEffect -> {
                        updateStatsUC.flow()
                    }

                    is SendNotificationEffect -> {
                        sendNotificationUC.flow(effect)
                    }
                }.collect { actions -> actions.forEach(handleAction) }
            }
        }
    }
}
