package com.spqrta.state.common.logic.features.dynalist

import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.state.common.BuildConfig
import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.logic.effect.AppEffect
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
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.time.toSeconds
import java.time.LocalDateTime

object Dynalist {
    const val DYNALIST_API_KEY = BuildConfig.DYNALIST_API_KEY

    const val TODO_DOCUMENT_ID = "omSjo4KOvqr1J4kNlysdo9In"
//    const val TODO_DOCUMENT_ID = "fj8owT2lQyd7nikhyRSJE1GK"

    const val TASKS_NODE_ID = "kjHpmZQSU7Hukgy_l1QrRLib"

    //    const val TASKS_NODE_ID = "TkPa2KwGiYEaOTbU2vGKuEM4"
    private val UPDATE_TIMEOUT = 1.toSeconds()

    val reducer = widen(
        typeGet(),
        AppStateOptics.optReady + AppReadyOptics.optDynalistState,
        ::reduce
    )

    private fun reduce(
        action: DynalistAction,
        state: DynalistState
    ): Reduced<out DynalistState, out AppEffect> {
        return when (state) {
            is DynalistState.Loaded -> {
                when (action) {
                    is DynalistAction.DynalistLoaded -> {
                        state.copy(loadedAt = LocalDateTime.now()).withEffects()
                    }

                    is ClockAction.TickAction -> {
                        if (
                            state.loadedAt
                                .plusSeconds(UPDATE_TIMEOUT.totalSeconds)
                                .isBefore(LocalDateTime.now())
                        ) {
                            state.withEffects(LoadDynalistEffect)
                        } else {
                            state.withEffects()
                        }
                    }
                }
            }

            is DynalistState.Initial -> {
                when (action) {
                    is DynalistAction.DynalistLoaded -> {
                        when (action.tasks) {
                            is Success -> {
                                DynalistState.Loaded(
                                    loadedAt = LocalDateTime.now(),
                                    elements = action.tasks.success.children.map { it.toElement() }
                                ).withEffects()
                            }

                            is Failure -> {
                                DynalistState.Loaded(
                                    loadedAt = LocalDateTime.now(),
                                    elements = emptyList()
                                ).withEffects()
                            }
                        }
                    }

                    is ClockAction.TickAction -> {
                        state.withEffects(LoadDynalistEffect)
                    }
                }
            }
        }
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