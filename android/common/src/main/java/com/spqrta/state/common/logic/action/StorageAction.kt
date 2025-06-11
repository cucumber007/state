package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.storage.StorageItem

sealed interface StorageAction : AppAction {
    sealed class Action : StorageAction
    data class AddItemAction(val item: StorageItem) : Action()
}
