package com.spqrta.state.common.logic.features.gtd2.element.routine

data class UpdateContextResult<Context : RoutineContext>(
    val newTrigger: RoutineTrigger<Context>,
    val shouldReset: Boolean,
)
