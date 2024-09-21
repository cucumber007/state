package com.spqrta.state.common

import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import org.junit.Test

class MetaStateTest {

    @Test
    fun test() {
        wrapLog {
            val metaState = MetaState.INITIAL
            println(metaState.prompts)
        }
    }
}
