package com.spqrta.state.common.logic.features.dynalist

import com.spqrta.state.common.util.optics.asOpticOptional
import kotlinx.serialization.Serializable

@Serializable
sealed class DynalistState {
    override fun toString(): String = javaClass.simpleName

    @Serializable
    object KeyNotSet : DynalistState()

    @Serializable
    data class DocsLoading(
        val key: String,
        val loadingState: DynalistLoadingState,
    ) : DynalistState()

    @Serializable
    data class CreatingDoc(
        val key: String,
    ) : DynalistState()

    @Serializable
    data class DocCreated(
        val key: String,
        val docId: String,
        val loadingState: DynalistLoadingState,
    ) : DynalistState()

    companion object {
        val optLoadedState = ({ state: DynalistState ->
            (state as? DocsLoading)?.loadingState
        } to { state: DynalistState, loadingState: DynalistLoadingState ->
            (state as? DocsLoading)?.copy(loadingState = loadingState) ?: state
        }).asOpticOptional()

        val INITIAL = KeyNotSet
    }
}
