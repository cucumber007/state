package com.spqrta.state.common.logic.effect

import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.PromptAction
import com.spqrta.state.common.use_case.UseCases
import com.spqrta.state.common.util.collections.asList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
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

                    is AppEffectNew.OpenUrl -> {
                        useCases.openUrlUC.flow(effect.url)
                    }


                    is AppEffectNew.StartFgs -> {
                        useCases.startFgsUC.flow()
                    }

                    is DynalistEffect -> {
                        getFlow(effect, useCases)
                    }

                    is LoadStateEffect -> {
                        loadStateUC.flow()
                    }

                    is PlayNotificationSoundEffect -> {
                        playNotificationSoundUC.flow()
                    }

                    is PlaySoundEffect -> {
                        playSoundUC.flow(effect.sound)
                    }

                    is SaveStateEffect -> {
                        saveStateUC.flow(effect.state)
                    }

                    is SendNotificationEffect -> {
                        sendNotificationUC.flow(effect)
                    }

                    is ShowToastEffect -> {
                        showToastUC.flow(effect.message)
                    }

                    is TickEffect -> {
                        tickUC.flow(effect.duration)
                    }

                    is UpdateStatsEffect -> {
                        updateStatsUC.flow()
                    }

                    is VibrateEffect -> {
                        vibrateUC.flow()
                    }

                    is ViewEffect.Scroll -> {
                        useCases.appScope.viewEffectsHandler(effect)
                    }
                }.collect { actions -> actions.forEach(handleAction) }
            }
        }
    }
}

private fun getFlow(effect: DynalistEffect, useCases: UseCases): Flow<List<AppAction>> {
    return when (effect) {
        is LoadDynalistEffect -> {
            useCases.loadDynalistUC.flow(effect.apiKey, effect.docId)
        }

        is DynalistEffect.GetDocs -> {
            useCases.getDynalistDocsUC.flow(effect.dynalistState.key)
        }

        is DynalistEffect.InitDoc -> {
            useCases.initDynalistDocUC.flow(
                apiKey = effect.dynalistState.key,
                rootId = effect.dynalistState.dynalistUserRootId
            )
        }
    }
}
