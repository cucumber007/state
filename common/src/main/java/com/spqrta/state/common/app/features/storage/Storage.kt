package com.spqrta.state.common.app.features.storage

import com.spqrta.state.common.app.AppEffect
import com.spqrta.state.common.app.action.StorageAction
import com.spqrta.state.common.app.state.optics.AppReadyOptics
import com.spqrta.state.common.util.optics.asOpticGet
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import kotlinx.serialization.Serializable

@Serializable
data class Storage(
    val food: Food = Food()
) {
    val allItems: List<StorageItem> by lazy {
        listOf(
            food.caloryPacks
        )
    }

    val notOkItems: List<StorageItem> by lazy {
        allItems.filter { !it.isOk() }
    }

    val displayString by lazy {
        allItems.map {
            val ok = if (it.isOk()) "OK" else "NOT OK"
            "${it.name}: ${it.amount}/${it.normal} = $ok"
        }.joinToString("\n")
    }

    companion object {
        val reducer = widen(
            typeGet(),
            AppReadyOptics.optStorage,
            Storage::reduce
        )

        private fun reduce(action: StorageAction, state: Storage): Reduced<Storage, out AppEffect> {
            return when (action) {
                is StorageAction.AddItemAction -> {
                    state.copy(
                        food = state.food.copy(
                            caloryPacks = state.food.caloryPacks.copy(
                                amount = state.food.caloryPacks.amount + 1
                            )
                        )
                    ).withEffects()
                }
            }
        }

        val optIsOk = { storage: Storage ->
            storage.notOkItems.isEmpty()
        }.asOpticGet()
    }
}
