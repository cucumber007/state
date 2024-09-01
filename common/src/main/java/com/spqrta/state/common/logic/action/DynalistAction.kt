package com.spqrta.state.common.logic.action

import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.state.common.util.Res


sealed interface DynalistAction : AppAction {
    sealed class Action : DynalistAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class DynalistDocsLoaded(
        val docIdResult: Res<String?>,
    ) : Action()

    data class DynalistLoaded(
        val tasks: Res<DynalistNode>,
    ) : Action()

    data class DynalistDatabaseDocCreated(
        val docIdResult: Res<String>,
    ) : Action()

    object OpenGetApiKeyPage : Action()

    data class SetApiKey(val key: String) : Action()
}
