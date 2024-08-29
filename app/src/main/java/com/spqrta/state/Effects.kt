package com.spqrta.state

import com.spqrta.state.common.logic.effect.ViewEffect
import kotlinx.coroutines.flow.MutableStateFlow

object Effects {

    val effects = MutableStateFlow<List<ViewEffect>>(emptyList())

}
