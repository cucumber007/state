package com.spqrta.state.common.logic.features.gtd2.tinder

import kotlinx.serialization.Serializable

@Serializable
data class TinderState(
    val prompts: List<TinderPrompt>,
    val skipped: List<TinderPrompt> = listOf()
) {
    companion object {
        val INITIAL = TinderState(
            prompts = listOf()
        )
    }
}