package com.spqrta.state.common

import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.div
import com.spqrta.state.common.util.state_machine.plus
import junit.framework.TestCase.assertEquals
import org.junit.Test

class FilterTest {

    @Test
    fun testFilter() {
        val reducer1 = { action: Int, state: String -> Reduced(state + "1", setOf(1f)) }
        val reducer2 = { action: Int, state: String -> Reduced(state + "2", setOf(2f)) }
        val reducer3 = { action: Int, state: String -> Reduced(state + "3", setOf(3f)) }
        assertEquals(
            Reduced(newState = "123", effects = setOf(3f, 2f, 1f)),
            (reducer1 + reducer2 + reducer3).invoke(0, ""),
        )

        val filter = { action: Int, state: String -> action != 0 }

        assertEquals(
            Reduced(newState = "", effects = setOf<Float>()),
            (filter / (reducer1 + reducer2 + reducer3)).invoke(0, ""),
        )
        assertEquals(
            Reduced(newState = "123", effects = setOf(3f, 2f, 1f)),
            (filter / (reducer1 + reducer2 + reducer3)).invoke(1, ""),
        )
    }
}
