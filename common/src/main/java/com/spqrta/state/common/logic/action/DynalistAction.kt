package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.dynalist.DocCreatedResult
import com.spqrta.state.common.logic.features.dynalist.DynalistStateAppDatabase
import com.spqrta.state.common.logic.features.dynalist.LoadDocsResult
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.util.result.Res


sealed interface DynalistAction : AppAction {
    sealed class Action : DynalistAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class OnTaskCompletedDynalist(val task: Task) : Action()

    data class DynalistDocsLoaded(
        val docsResult: Res<LoadDocsResult>,
    ) : Action()

    data class DynalistLoaded(
        val docResult: Res<DynalistStateAppDatabase>,
    ) : Action()

    data class DynalistDatabaseDocCreated(
        val docResult: Res<DocCreatedResult>,
    ) : Action()

    object OpenGetApiKeyPage : Action()

    data class SetApiKey(val key: String) : Action()
}
