package com.spqrta.state.common.logic.features.dynalist

import com.spqrta.state.common.BuildConfig
import com.spqrta.state.common.environments.DateTimeEnvironment
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
    data class Error(
        val key: String,
        val error: String
    ) : DynalistState()

    @Serializable
    data class CreatingDoc(
        val key: String,
        val dynalistUserRootId: String,
    ) : DynalistState()

    @Serializable
    data class DocCreated(
        val key: String,
        val databaseDocId: String,
        val loadingState: DynalistLoadingState,
        val periodicUpdatesEnabled: Boolean,
    ) : DynalistState() {
        companion object {
            fun initial(
                key: String,
                databaseDocId: String,
                database: DynalistStateAppDatabase
            ) = DynalistState.DocCreated(
                key = key,
                databaseDocId = databaseDocId,
                loadingState = DynalistLoadingState.Loaded(
                    loadedAt = DateTimeEnvironment.dateTimeNow,
                    database = database
                ),
                periodicUpdatesEnabled = true
            )
        }
    }

    fun periodicUpdatesEnabledText(): String {
        return when (this) {
            is DocCreated -> {
                if (periodicUpdatesEnabled) {
                    "Enabled"
                } else {
                    "Disabled"
                }
            }

            is Error -> {
                "Error"
            }

            else -> {
                "Updating"
            }
        }
    }

    companion object {
        val optCreatingDoc = ({ state: DynalistState ->
            state as? CreatingDoc
        } to { _: DynalistState, subState: DynalistState ->
            subState
        }).asOpticOptional()

        val optDocCreated = ({ state: DynalistState ->
            state as? DocCreated
        } to { _: DynalistState, subState: DynalistState ->
            subState
        }).asOpticOptional()

        val optDocsLoading = ({ state: DynalistState ->
            state as? DocsLoading
        } to { _: DynalistState, subState: DynalistState ->
            subState
        }).asOpticOptional()

        val optKeyNotSet = ({ state: DynalistState ->
            state as? KeyNotSet
        } to { _: DynalistState, subState: DynalistState ->
            subState
        }).asOpticOptional()

        val optLoadingState = ({ state: DynalistState ->
            (state as? DocCreated)?.loadingState
        } to { state: DynalistState, loadingState: DynalistLoadingState ->
            (state as? DocCreated)?.copy(loadingState = loadingState) ?: state
        }).asOpticOptional()

        val INITIAL = if (!BuildConfig.DEBUG) {
            KeyNotSet
        } else {
            DocsLoading(
                "Stq27PzrRWqpu1zMPC4NG-bAbNbvgrKa-JHPF5LjKqEGv-o-vYVThopjX5HKAMcWjvPBvH-W-bA_TrpQipuIAPjkYveMf-91DInFmJ4OAG9Y0Ar7JL0HCtzrpS374NUO"
            )
        }
    }
}
