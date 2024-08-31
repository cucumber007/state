package com.spqrta.state.common.logic.features.dynalist

import com.spqrta.state.common.util.optics.asOpticOptional
import kotlinx.serialization.Serializable

@Serializable
sealed class DynalistState {
    override fun toString(): String = javaClass.simpleName

    @Serializable
    object KeyNotSet : DynalistState()

    @Serializable
    data class KeySet(
        val key: String,
        val loadingState: DynalistLoadingState,
    ) : DynalistState()

    companion object {
        val optLoadedState = ({ state: DynalistState ->
            (state as? KeySet)?.loadingState
        } to { state: DynalistState, loadingState: DynalistLoadingState ->
            (state as? KeySet)?.copy(loadingState = loadingState) ?: state
        }).asOpticOptional()

        val INITIAL = KeyNotSet
    }
}
