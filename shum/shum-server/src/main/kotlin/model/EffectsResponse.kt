package model

import kotlinx.serialization.Serializable

@Serializable
data class EffectsResponse(
    val effects: List<RemoteEffect>
)