package com.spqrta.state.common.app.action

import com.spqrta.state.common.app.features.storage.StorageItem

sealed interface StorageAction : AppAction {
    sealed class Action : StorageAction
    data class AddItemAction(val item: StorageItem) : Action()
}
