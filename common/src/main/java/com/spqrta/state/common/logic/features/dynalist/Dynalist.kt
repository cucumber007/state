package com.spqrta.state.common.logic.features.dynalist

import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.AppEffectNew
import com.spqrta.state.common.logic.effect.DynalistEffect
import com.spqrta.state.common.logic.effect.LoadDynalistEffect
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.Failure
import com.spqrta.state.common.util.Success
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.Reducer
import com.spqrta.state.common.util.state_machine.illegalAction
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.time.toMinutes
import java.time.LocalDateTime

object Dynalist {
    private const val API_KEY_URL = "https://dynalist.io/developer"
    const val TODO_DOCUMENT_ID = "omSjo4KOvqr1J4kNlysdo9In"
    //    const val TODO_DOCUMENT_ID = "fj8owT2lQyd7nikhyRSJE1GK"

    const val TASKS_NODE_ID = "kjHpmZQSU7Hukgy_l1QrRLib"

    //    const val TASKS_NODE_ID = "TkPa2KwGiYEaOTbU2vGKuEM4"
    private val UPDATE_TIMEOUT = 100.toMinutes()

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
        if (action is DebugAction.ResetDay) {
            return handleAction(action)
        }
        if (action is ClockAction.TickAction && state !is DynalistState.DocCreated) {
            return state.withEffects()
        }

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
                            val newState = DynalistState.DocsLoading(
                                key = action.key,
                                loadingState = DynalistLoadingState.Initial
                            )
                            newState.withEffects(
                                DynalistEffect.GetDocs(newState)
                            )
                        } else {
                            state.withEffects()
                        }
                    }

                    is ClockAction.TickAction,
                    is DynalistAction.DynalistDatabaseDocCreated,
                    is DebugAction.ResetDay,
                    is DynalistAction.DynalistDocsLoaded,
                    is DynalistAction.DynalistLoaded -> {
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
                                val docId = action.docIdResult.success
                                if (docId != null) {
                                    DynalistState.DocCreated(
                                        key = state.key,
                                        docId = docId,
                                        loadingState = DynalistLoadingState.Initial
                                    ).withEffects()
                                } else {
                                    val newState = DynalistState.CreatingDoc(
                                        key = state.key
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

                    is DynalistAction.DynalistDatabaseDocCreated,
                    is DebugAction.ResetDay,
                    is DynalistAction.DynalistLoaded,
                    is DynalistAction.OpenGetApiKeyPage,
                    is DynalistAction.SetApiKey,
                    is ClockAction.TickAction -> {
                        illegalAction(action, state)
                    }
                }
            }

            is DynalistState.CreatingDoc -> {
                // sorting actions by importance
                when (action) {
                    is DynalistAction.DynalistDatabaseDocCreated -> {
                        TODO()
                    }

                    is DynalistAction.DynalistDocsLoaded,
                    is DynalistAction.DynalistLoaded,
                    is DynalistAction.OpenGetApiKeyPage,
                    is DynalistAction.SetApiKey,
                    is DebugAction.ResetDay,
                    is ClockAction.TickAction -> {
                        illegalAction(action, state)
                    }
                }
            }

            is DynalistState.DocCreated -> {
                when (action) {
                    is ClockAction.TickAction -> {
                        when (state.loadingState) {
                            is DynalistLoadingState.Initial -> {
                                state.withEffects(LoadDynalistEffect(state))
                            }

                            is DynalistLoadingState.Loaded -> {
                                if (state.loadingState.loadedAt.plusSeconds(UPDATE_TIMEOUT.totalSeconds)
                                        .isBefore(LocalDateTime.now())
                                ) {
                                    state.loadingState.withEffects(LoadDynalistEffect(state))
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
                                when (action.tasks) {
                                    is Success -> {
                                        DynalistLoadingState.Loaded(
                                            loadedAt = LocalDateTime.now(),
                                            elements = action.tasks.success.children.map { it.toElement() }
                                        ).withEffects<DynalistLoadingState, AppEffect>()
                                    }

                                    is Failure -> {
                                        DynalistLoadingState.Loaded(
                                            loadedAt = LocalDateTime.now(),
                                            elements = emptyList()
                                        ).withEffects()
                                    }
                                }.flatMapState {
                                    DynalistState.optLoadedState.set(state, it.newState)
                                }
                            }

                            is DynalistLoadingState.Loaded -> {
                                val oldLoadingState = state.loadingState
                                oldLoadingState.copy(loadedAt = LocalDateTime.now())
                                    .withEffects<DynalistLoadingState, AppEffect>()
                                    .flatMapState {
                                        DynalistState.optLoadedState.set(state, it.newState)
                                    }
                            }
                        }
                    }

                    is DynalistAction.DynalistDatabaseDocCreated -> TODO()
                    is DynalistAction.DynalistDocsLoaded -> TODO()
                    is DynalistAction.OpenGetApiKeyPage -> TODO()
                    is DynalistAction.SetApiKey -> TODO()
                    is DebugAction.ResetDay -> TODO()
                }
            }
        }
    }

    private fun handleAction(action: DebugAction.ResetDay): Reduced<DynalistState, AppEffect> {
        return DynalistState.INITIAL.withEffects()
    }

    private fun DynalistNode.toElement(): Element {
        return if (this.children.isNotEmpty()) {
            Queue(
                name = this.title,
                elements = this.children.map { it.toElement() }
            )
        } else {
            Task(
                name = this.title,
            )
        }
    }

}
