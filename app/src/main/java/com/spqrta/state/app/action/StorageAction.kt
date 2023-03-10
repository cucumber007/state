package com.spqrta.state.app.action

import com.spqrta.state.app.features.storage.StorageItem

sealed interface StorageAction: AppAction {
    sealed class Action : StorageAction
    data class AddItemAction(val item: StorageItem) : Action()
}
