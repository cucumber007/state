package com.spqrta.state.common

import com.spqrta.state.common.util.state_machine.StateMachine
import com.spqrta.state.common.util.state_machine.effectIf
import com.spqrta.state.common.util.state_machine.withEffects
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@Suppress("OPT_IN_USAGE")
class AsyncStateMachineTest {

    private lateinit var stateMachine: StateMachine<Int, String, Float>
    private val sentEffects = mutableListOf<Float>()

    @Test
    fun test() = wrapLog {
        runTest {

            val testScope = TestScope()

            stateMachine = object : StateMachine<Int, String, Float>(
                tag = "AsyncStateMachineTest",
                initialState = "0",
                scope = testScope,
                reducer = { action, state ->
                    val newState = state + action
                    newState.withEffects(effectIf(action < 100) { newState.toFloat() })
                },
                applyEffects = { effects ->
                    effects.forEach { effect ->
                        testScope.launch {
                            stateMachine.handleAction(effect.toInt())
                            sentEffects.add(effect)
                        }
                    }
                }
            ) {
                override fun log(txt: String) {
                    println(txt)
                }
            }
            stateMachine.handleAction(1)

            testScope.advanceUntilIdle()

            assertEquals("011111111", stateMachine.state.value)
            assertEquals(listOf(1f, 11f, 1111f), sentEffects)
        }
    }

}
