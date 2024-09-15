package com.spqrta.state.common.logic.features.dynalist

import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.action.StateLoadedAction
import com.spqrta.state.common.logic.effect.ActionEffect
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.AppEffectNew
import com.spqrta.state.common.logic.effect.DynalistEffect
import com.spqrta.state.common.logic.effect.LoadDynalistEffect
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.Failure
import com.spqrta.state.common.util.Success
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.Reducer
import com.spqrta.state.common.util.state_machine.chain
import com.spqrta.state.common.util.state_machine.illegalAction
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.time.toDays
import java.time.LocalDateTime

object Dynalist {
    private const val API_KEY_URL = "https://dynalist.io/developer"
    private val UPDATE_TIMEOUT = 1.toDays()

    val reducer = widen(
        typeGet(),
        AppStateOptics.optReady + AppReadyOptics.optDynalistState,
        ::reduce
    )

    val viewReducer: Reducer<AppAction, AppState, AppEffect> = { action, state ->
        state.withEffects()
    }

    private fun reduce(
        action: DynalistAction,
        state: DynalistState
    ): Reduced<out DynalistState, out AppEffect> {
        return when {
            action is DebugAction.ResetDay -> handleAction(action)
            action is ClockAction.TickAction && state !is DynalistState.DocCreated -> state.withEffects()
            action is StateLoadedAction -> handleAction(action)
            else -> reduceRest(action, state)
        }
    }

    private fun reduceRest(
        action: DynalistAction,
        state: DynalistState
    ): Reduced<out DynalistState, out AppEffect> {
        // sorting states by order
        return when (state) {
            is DynalistState.KeyNotSet -> {
                // sorting actions by importance
                when (action) {
                    is DynalistAction.OpenGetApiKeyPage -> {
                        state.withEffects(AppEffectNew.OpenUrl(API_KEY_URL))
                    }

                    is DynalistAction.SetApiKey -> {
                        if (action.key.isNotEmpty()) {
                            val newState = DynalistState.DocsLoading(key = action.key)
                            newState.withEffects(
                                DynalistEffect.GetDocs(newState)
                            )
                        } else {
                            state.withEffects()
                        }
                    }

                    is DebugAction.UpdateDynalist -> {
                        state.withEffects()
                    }

                    is ClockAction.TickAction,
                    is DynalistAction.DynalistDatabaseDocCreated,
                    is DebugAction.ResetDay,
                    is DynalistAction.DynalistDocsLoaded,
                    is DynalistAction.DynalistLoaded,
                    is StateLoadedAction -> {
                        illegalAction(action, state)
                    }
                }
            }

            is DynalistState.DocsLoading -> {
                // sorting actions by importance
                when (action) {
                    is DynalistAction.DynalistDocsLoaded -> {
                        when (action.docIdResult) {
                            is Success -> {
                                val result = action.docIdResult.success
                                if (result.stateAppDatabaseDocData != null) {
                                    val (stateDocId, stateDoc) = result.stateAppDatabaseDocData
                                    DynalistState.DocCreated(
                                        key = state.key,
                                        stateDocId = stateDocId,
                                        loadingState = DynalistLoadingState.Loaded(
                                            loadedAt = LocalDateTime.now(),
                                            nodes = stateDoc.children
                                        )
                                    ).withEffects()
                                } else {
                                    val newState = DynalistState.CreatingDoc(
                                        key = state.key,
                                        rootId = action.docIdResult.success.rootId
                                    )
                                    newState.withEffects(
                                        DynalistEffect.CreateDoc(newState)
                                    )
                                }
                            }

                            is Failure -> {
                                state.withEffects()
                            }
                        }
                    }

                    is DebugAction.UpdateDynalist -> {
                        state.withEffects()
                    }

                    is DynalistAction.DynalistDatabaseDocCreated,
                    is DebugAction.ResetDay,
                    is DynalistAction.DynalistLoaded,
                    is DynalistAction.OpenGetApiKeyPage,
                    is DynalistAction.SetApiKey,
                    is ClockAction.TickAction,
                    is StateLoadedAction -> {
                        illegalAction(action, state)
                    }
                }
            }

            is DynalistState.CreatingDoc -> {
                // sorting actions by importance
                when (action) {
                    is DynalistAction.DynalistDatabaseDocCreated -> {
                        when (action.docResult) {
                            is Failure -> {
                                state.withEffects()
                            }

                            is Success -> {
                                DynalistState.DocCreated(
                                    key = state.key,
                                    stateDocId = action.docResult.success.docId,
                                    loadingState = DynalistLoadingState.Loaded(
                                        loadedAt = LocalDateTime.now(),
                                        nodes = action.docResult.success.doc.children
                                    )
                                ).withEffects()
                            }
                        }
                    }

                    is DebugAction.UpdateDynalist -> {
                        state.withEffects()
                    }

                    is DynalistAction.DynalistDocsLoaded,
                    is DynalistAction.DynalistLoaded,
                    is DynalistAction.OpenGetApiKeyPage,
                    is DynalistAction.SetApiKey,
                    is DebugAction.ResetDay,
                    is ClockAction.TickAction,
                    is StateLoadedAction -> {
                        illegalAction(action, state)
                    }
                }
            }

            is DynalistState.DocCreated -> {
                when (action) {
                    is ClockAction.TickAction -> {
                        when (val loadingState = state.loadingState) {
                            is DynalistLoadingState.Initial -> {
                                state.withEffects(LoadDynalistEffect(state.key, state.stateDocId))
                            }

                            is DynalistLoadingState.Loaded -> {
                                if (loadingState.loadedAt.plusSeconds(UPDATE_TIMEOUT.totalSeconds)
                                        .isBefore(LocalDateTime.now())
                                ) {
                                    state.loadingState.withEffects(
                                        LoadDynalistEffect(
                                            state.key,
                                            state.stateDocId
                                        )
                                    )
                                } else {
                                    state.loadingState.withEffects()
                                }.flatMapState {
                                    DynalistState.optLoadedState.set(state, it.newState)
                                }
                            }
                        }
                    }

                    is DynalistAction.DynalistLoaded -> {
                        when (state.loadingState) {
                            is DynalistLoadingState.Initial -> {
                                when (action.docResult) {
                                    is Success -> {
                                        DynalistLoadingState.Loaded(
                                            loadedAt = LocalDateTime.now(),
                                            nodes = action.docResult.success.children
                                        ).withEffects<DynalistLoadingState, AppEffect>()
                                    }

                                    is Failure -> {
                                        DynalistLoadingState.Loaded(
                                            loadedAt = LocalDateTime.now(),
                                            nodes = emptyList()
                                        ).withEffects()
                                    }
                                }.flatMapState {
                                    DynalistState.optLoadedState.set(state, it.newState)
                                }
                            }

                            is DynalistLoadingState.Loaded -> {
                                val oldLoadingState = state.loadingState
                                val newLoadingState = DynalistLoadingState.Loaded(
                                    loadedAt = LocalDateTime.now(),
                                    nodes = action.docResult.let {
                                        when (it) {
                                            is Success -> it.success.children
                                            is Failure -> listOf()
                                        }
                                    }
                                )
                                val newDynalistState = DynalistState.optLoadedState.set(
                                    state,
                                    newLoadingState
                                )
                                newDynalistState.withEffects(
                                    ActionEffect(
                                        Gtd2Action.DynalistStateUpdated(newDynalistState)
                                    )
                                )
                            }
                        }
                    }

                    is DebugAction.UpdateDynalist -> {
                        state.withEffects(
                            LoadDynalistEffect(
                                state.key,
                                state.stateDocId
                            )
                        )
                    }

                    is DynalistAction.DynalistDatabaseDocCreated,
                    is DynalistAction.DynalistDocsLoaded,
                    is DynalistAction.OpenGetApiKeyPage,
                    is DynalistAction.SetApiKey,
                    is DebugAction.ResetDay,
                    is StateLoadedAction -> {
                        illegalAction(action, state)
                    }
                }
            }
        }
    }

    private fun handleAction(action: DebugAction.ResetDay): Reduced<out DynalistState, out AppEffect> {
        return chain(
            DynalistState.INITIAL.withEffects<DynalistState, AppEffect>()
        ) {
            onStateLoaded(it)
        }
    }

    private fun handleAction(action: StateLoadedAction): Reduced<DynalistState, AppEffect> {
        return onStateLoaded(action.state.dynalistState)
    }

    private fun onStateLoaded(dynalistState: DynalistState): Reduced<DynalistState, AppEffect> {
        return when (dynalistState) {
            is DynalistState.CreatingDoc -> {
                dynalistState.withEffects(
                    DynalistEffect.CreateDoc(
                        dynalistState
                    )
                )
            }

            is DynalistState.DocCreated -> {
                dynalistState.withEffects(
                    ActionEffect(
                        Gtd2Action.DynalistStateUpdated(dynalistState)
                    )
                )
            }

            is DynalistState.DocsLoading -> {
                dynalistState.withEffects(
                    DynalistEffect.GetDocs(
                        dynalistState
                    )
                )
            }

            is DynalistState.KeyNotSet -> {
                dynalistState.withEffects()
            }
        }
    }

}
