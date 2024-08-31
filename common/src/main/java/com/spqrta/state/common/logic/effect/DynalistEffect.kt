package com.spqrta.state.common.logic.effect

import com.spqrta.state.common.logic.features.dynalist.DynalistState

data class LoadDynalistEffect(val dynalistState: DynalistState.KeySet) : AppEffect {
    override fun toString(): String = javaClass.simpleName
}
