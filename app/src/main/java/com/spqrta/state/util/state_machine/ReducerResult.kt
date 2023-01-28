package com.spqrta.state.util.state_machine

open class ReducerResult<State, Effect>(val newState: State, val effects: Set<Effect>) {
    override fun toString(): String {
        return "${javaClass.simpleName}(newState=$newState, effects=$effects)"
    }

    fun <S1> flatMap(mapFunction: (ReducerResult<State, Effect>) -> ReducerResult<S1, Effect>): ReducerResult<S1, Effect> {
        return mapFunction(this)
    }

    fun <NewState> flatMapState(mapFunction: (ReducerResult<State, Effect>) -> NewState): ReducerResult<NewState, Effect> {
        return ReducerResult(mapFunction(this), this.effects)
    }

    fun <NewEffect> flatMapEffects(mapFunction: (ReducerResult<State, Effect>) -> Set<NewEffect>): ReducerResult<State, NewEffect> {
        return ReducerResult(this.newState, mapFunction(this))
    }
}

fun <State, Effect> chain(
    firstResult: ReducerResult<out State, out Effect>,
    secondResult: (State) -> ReducerResult<out State, out Effect>,
): ReducerResult<out State, out Effect> {
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
    firstResult: ReducerResult<out State1, out Effect>,
    otherResult: (ReducerResult<out State1, out Effect>) -> ReducerResult<out State2, out Effect>,
    mergeFunction: (state1: State1, state2: State2) -> MergedState
): ReducerResult<out MergedState, out Effect> {
    val result = otherResult.invoke(firstResult)
    return ReducerResult(
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
