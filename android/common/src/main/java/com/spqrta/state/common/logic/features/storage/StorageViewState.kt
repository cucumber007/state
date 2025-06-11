package com.spqrta.state.common.logic.features.storage

import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.StorageAction
import com.spqrta.state.common.logic.features.daily.personas.Persona
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.ui.view.control.Button
import com.spqrta.state.common.ui.view_state.ButtonForm
import com.spqrta.state.common.ui.view_state.ViewState

fun getStorageViewState(state: AppReady, persona: Persona): ViewState {
    val storage = AppReadyOptics.optStorage.getStrict(state)
    val notOkItems = storage.notOkItems
    return ButtonForm(
        text = "Storage is not OK\n\n${storage.displayString}", buttons = notOkItems.map {
            Button(
                text = it.name,
                action = StorageAction.AddItemAction(it)
            )
        }
    )
}