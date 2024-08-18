package com.spqrta.state.common.util.state_machine

open class Reduced<State, Effect>(val newState: State, val effects: Set<Effect>) {
    override fun toString(): String {
        return "${javaClass.simpleName}(newState=$newState, effects=$effects)"
    }

    fun <S1> flatMap(mapFunction: (Reduced<State, Effect>) -> Reduced<S1, Effect>): Reduced<S1, Effect> {
        return mapFunction(this)
    }

    fun <NewState> flatMapState(mapFunction: (Reduced<State, Effect>) -> NewState): Reduced<NewState, Effect> {
        return Reduced(mapFunction(this), this.effects)
    }

    fun <NewEffect> flatMapEffects(mapFunction: (Reduced<State, Effect>) -> Set<NewEffect>): Reduced<State, NewEffect> {
        return Reduced(this.newState, mapFunction(this))
    }

    override fun hashCode(): Int {
        return newState.hashCode() + effects.joinToString("#").hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return (other as Reduced<State, Effect>).let { otherTyped ->
            otherTyped.newState == newState && otherTyped.effects == effects
        }
    }
}

fun <State, Effect> chain(
    firstResult: Reduced<out State, out Effect>,
    secondResult: (State) -> Reduced<out State, out Effect>,
): Reduced<out State, out Effect> {
    return mergeResults(
        firstResult,
        {
            secondResult.invoke(it.newState)
        }
    ) { _, state2 ->
        state2
    }
}

// merge two results with different states into one
fun <State1, State2, MergedState, Effect> mergeResults(
    firstResult: Reduced<out State1, out Effect>,
    otherResult: (Reduced<out State1, out Effect>) -> Reduced<out State2, out Effect>,
    mergeFunction: (state1: State1, state2: State2) -> MergedState
): Reduced<out MergedState, out Effect> {
    val result = otherResult.invoke(firstResult)
    return Reduced(
        mergeFunction.invoke(firstResult.newState, result.newState),
        firstResult.effects + result.effects
    )
}

fun <T> effectsIf(condition: Boolean, effects: () -> Set<T>): Set<T> {
    return if (condition) {
        effects.invoke()
    } else {
        setOf()
    }
}

fun <T> effectIf(condition: Boolean, effects: () -> T): Set<T> {
    return if (condition) {
        setOf(effects.invoke())
    } else {
        setOf()
    }
}

fun <T, E> effectIfNotNull(value: E?, effects: (value: E) -> T): Set<T> {
    return if (value != null) {
        setOf(effects.invoke(value))
    } else {
        setOf()
    }
}
