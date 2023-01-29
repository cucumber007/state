package com.spqrta.state.app.state

import kotlinx.serialization.Serializable

@Serializable
class Stats {
    override fun toString(): String = javaClass.simpleName
}
