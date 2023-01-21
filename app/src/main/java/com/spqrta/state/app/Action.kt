package com.spqrta.state.app


sealed class Action {
    override fun toString(): String = javaClass.simpleName
}
