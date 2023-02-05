package com.spqrta.state.app.features.daily.prompts

import com.spqrta.state.app.Prompt
import com.spqrta.state.app.TimeredPrompt
import com.spqrta.state.app.features.daily.timers.WorkTimer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test

@Suppress("SpellCheckingInspection")
internal class PromptTest {

    @Test
    fun testSerialization() {
        val json = Json {
            allowStructuredMapKeys = true
        }
        val prompts: List<Prompt> = listOf(TimeredPrompt(WorkTimer))
        val str = json.encodeToString(prompts)
        println(str)
        val res = json.decodeFromString<List<Prompt>>(str)
        println(res)
    }

}