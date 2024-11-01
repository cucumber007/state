package com.spqrta.state.common.logic.features.dynalist

import android.annotation.SuppressLint
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.AppReadyAction
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
import com.spqrta.state.common.util.optics.identityOptional
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.optics.withSubState
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.Reducer
import com.spqrta.state.common.util.state_machine.chain
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.state_machine.withOptic
import com.spqrta.state.common.util.time.toDays
import com.spqrta.state.common.util.toIso
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

    @SuppressLint("NewApi")
    private fun reduce(
        action: DynalistAction,
        state: DynalistState
    ): Reduced<out DynalistState, out AppEffect> {
        return when (action) {
            is ClockAction.TickAction -> {
                withOptic(
                    action,
                    state,
                    DynalistState.optDocCreated,
                    identityOptional(),
                ) { docCreated: DynalistState.DocCreated ->
                    when (val loadingState = docCreated.loadingState) {
                        is DynalistLoadingState.Initial -> {
                            docCreated.withEffects(
                                LoadDynalistEffect(
                                    docCreated.key,
                                    docCreated.databaseDocId
                                )
                            )
                        }

                        is DynalistLoadingState.Loaded -> {
                            if (loadingState.loadedAt.plusSeconds(UPDATE_TIMEOUT.totalSeconds)
                                    .isBefore(LocalDateTime.now())
                            ) {
                                docCreated.loadingState.withEffects(
                                    LoadDynalistEffect(
                                        docCreated.key,
                                        docCreated.databaseDocId
                                    ) as AppEffect
                                )
                            } else {
                                docCreated.loadingState.withEffects()
                            }.flatMapState {
                                DynalistState.optLoadingState.set(docCreated, it.newState)
                            }
                        }
                    }
                }
            }

            is DebugAction.ResetState -> {
                handleAction(action)
            }

            is DebugAction.UpdateDynalist -> {
                withOptic(
                    action,
                    state,
                    DynalistState.optDocCreated,
                    identityOptional(),
                    failIfNotApplicable = { true }
                ) { docCreated ->
                    docCreated.withEffects(
                        LoadDynalistEffect(
                            docCreated.key,
                            docCreated.databaseDocId
                        )
                    )
                }
            }

            is DynalistAction.DynalistDatabaseDocCreated -> {
                withOptic(
                    action as DynalistAction,
                    state,
                    DynalistState.optCreatingDoc,
                    identityOptional(),
                    failIfNotApplicable = { true }
                ) { creatingDoc ->
                    when (action.docResult) {
                        is Failure -> {
                            creatingDoc.withEffects()
                        }

                        is Success -> {
                            DynalistState.DocCreated(
                                key = creatingDoc.key,
                                databaseDocId = action.docResult.success.databaseDocId,
                                loadingState = DynalistLoadingState.Loaded(
                                    loadedAt = LocalDateTime.now(),
                                    database = action.docResult.success.database
                                )
                            ).withEffects()
                        }
                    }
                }
            }

            is DynalistAction.DynalistDocsLoaded -> {
                withOptic(
                    action as DynalistAction,
                    state,
                    DynalistState.optDocsLoading,
                    identityOptional(),
                    failIfNotApplicable = { true }
                ) { docsLoading ->
                    when (action.docsResult) {
                        is Success -> {
                            val result = action.docsResult.success
                            if (result.stateAppDatabaseDocData != null) {
                                val (databaseDocId, database) = result.stateAppDatabaseDocData
                                val newState = DynalistState.DocCreated(
                                    key = docsLoading.key,
                                    databaseDocId = databaseDocId,
                                    loadingState = DynalistLoadingState.Loaded(
                                        loadedAt = LocalDateTime.now(),
                                        database = database
                                    )
                                )
                                newState.withEffects(
                                    ActionEffect(
                                        Gtd2Action.DynalistStateUpdated(newState)
                                    )
                                )
                            } else {
                                val newState = DynalistState.CreatingDoc(
                                    key = docsLoading.key,
                                    dynalistUserRootId = action.docsResult.success.dynalistUserRootId
                                )
                                newState.withEffects(
                                    DynalistEffect.InitDoc(newState)
                                )
                            }
                        }

                        is Failure -> {
                            docsLoading.withEffects()
                        }
                    }
                }
            }

            is DynalistAction.DynalistLoaded -> {
                withOptic(
                    action as DynalistAction,
                    state,
                    DynalistState.optDocCreated,
                    identityOptional(),
                    failIfNotApplicable = { true }
                ) { docCreated ->
                    when (docCreated.loadingState) {
                        is DynalistLoadingState.Initial -> {
                            when (action.docResult) {
                                is Success -> {
                                    DynalistLoadingState.Loaded(
                                        loadedAt = LocalDateTime.now(),
                                        database = action.docResult.success
                                    ).withEffects<DynalistLoadingState, AppEffect>()
                                }

                                is Failure -> {
                                    docCreated.loadingState.withEffects(
                                        ActionEffect(AppReadyAction.ShowErrorAction(action.docResult.failure))
                                    )
                                }
                            }.flatMapState {
                                DynalistState.optLoadingState.set(docCreated, it.newState)
                            }
                        }

                        is DynalistLoadingState.Loaded -> {
                            when(action.docResult) {
                                is Failure -> {
                                    docCreated.withEffects(
                                        ActionEffect(AppReadyAction.ShowErrorAction(action.docResult.failure))
                                    )
                                }
                                is Success -> {
                                    val newLoadingState = DynalistLoadingState.Loaded(
                                        loadedAt = LocalDateTime.now(),
                                        database = action.docResult.success
                                    )
                                    val newDynalistState = DynalistState.optLoadingState.set(
                                        docCreated,
                                        newLoadingState
                                    )
                                    newDynalistState.withEffects()
                                }
                            }
                        }
                    }.flatMapEffects {
                        it.effects + ActionEffect(
                            Gtd2Action.DynalistStateUpdated(it.newState)
                        )
                    }
                }
            }

            is DynalistAction.OnTaskCompleted -> {
                withSubState(
                    state,
                    DynalistState.optDocCreated,
                    DynalistState.optLoadingState + DynalistLoadingState.optLoaded,
                    onNull = state.withEffects()
                ) { docCreated, loaded ->
                    docCreated.withEffects(
                        DynalistEffect.AddNode(
                            dynalistState = docCreated,
                            node = "${LocalDateTime.now().toIso()} - ${action.task.name}",
                            parentId = loaded.database.completedStorage.id
                        )
                    )
                }
            }

            is DynalistAction.OpenGetApiKeyPage -> {
                withOptic(
                    action as DynalistAction,
                    state,
                    DynalistState.optKeyNotSet,
                    identityOptional(),
                    failIfNotApplicable = { true },
                ) {
                    state.withEffects(AppEffectNew.OpenUrl(API_KEY_URL))
                }
            }

            is DynalistAction.SetApiKey -> {
                withOptic(
                    action as DynalistAction,
                    state,
                    DynalistState.optKeyNotSet,
                    identityOptional(),
                    failIfNotApplicable = { true }
                ) {
                    if (action.key.isNotEmpty()) {
                        val newState = DynalistState.DocsLoading(key = action.key)
                        newState.withEffects(
                            DynalistEffect.GetDocs(newState)
                        )
                    } else {
                        state.withEffects()
                    }
                }
            }

            is StateLoadedAction -> {
                handleAction(action)
            }
        }
    }

    private fun handleAction(action: DebugAction.ResetState): Reduced<out DynalistState, out AppEffect> {
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
                    DynalistEffect.InitDoc(
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
