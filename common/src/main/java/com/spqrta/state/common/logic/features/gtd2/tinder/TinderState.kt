package com.spqrta.state.common.logic.features.gtd2.tinder

import kotlinx.serialization.Serializable

@Serializable
data class TinderState(
    val prompts: List<TinderPrompt>,
) {
    companion object {
        val INITIAL = TinderState(
            prompts = listOf()
        )
    }
}