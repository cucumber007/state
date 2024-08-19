package com.spqrta.state.common

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainTest {
    @Test
    fun test() = runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)
        val stateMachine = loadedStateMachine(scope)
        waitFor(scope)
    }
}

suspend fun waitFor(scope: CoroutineScope) {
    val scopeJob = scope.coroutineContext[Job]!!
    while (scopeJob.isActive) {
        val children = scopeJob.children.toList()
        if (children.isEmpty()) break
        delay(1000)
    }
}
