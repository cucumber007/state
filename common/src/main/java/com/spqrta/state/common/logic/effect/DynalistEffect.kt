package com.spqrta.state.common.logic.effect

import com.spqrta.state.common.logic.features.dynalist.DynalistState

sealed interface DynalistEffect : AppEffect {
    data class GetDocs(val dynalistState: DynalistState.DocsLoading) : DynalistEffect
    data class InitDoc(val dynalistState: DynalistState.CreatingDoc) : DynalistEffect
}

data class LoadDynalistEffect(val apiKey: String, val docId: String) : DynalistEffect {
    override fun toString(): String = javaClass.simpleName
}
