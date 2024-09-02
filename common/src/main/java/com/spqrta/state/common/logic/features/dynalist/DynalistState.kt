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
        val key: String
    ) : DynalistState()

    @Serializable
    data class CreatingDoc(
        val key: String,
        val rootId: String,
    ) : DynalistState()

    @Serializable
    data class DocCreated(
        val key: String,
        val docId: String,
        val loadingState: DynalistLoadingState,
    ) : DynalistState()

    companion object {
        val optLoadedState = ({ state: DynalistState ->
            (state as? DocCreated)?.loadingState
        } to { state: DynalistState, loadingState: DynalistLoadingState ->
            (state as? DocCreated)?.copy(loadingState = loadingState) ?: state
        }).asOpticOptional()

        val INITIAL = DocsLoading(
            "Stq27PzrRWqpu1zMPC4NG-bAbNbvgrKa-JHPF5LjKqEGv-o-vYVThopjX5HKAMcWjvPBvH-W-bA_TrpQipuIAPjkYveMf-91DInFmJ4OAG9Y0Ar7JL0HCtzrpS374NUO"
        )
    }
}
