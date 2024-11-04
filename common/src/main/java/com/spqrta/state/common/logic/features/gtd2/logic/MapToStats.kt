package com.spqrta.state.common.logic.features.gtd2.logic

import android.annotation.SuppressLint
import com.spqrta.state.common.logic.features.gtd2.TasksDatabaseState
import com.spqrta.state.common.logic.features.gtd2.TasksState
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.util.time.PositiveSeconds
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import java.time.LocalTime

fun mapToStats(
    tasksState: TasksState,
    tasksDatabaseState: TasksDatabaseState
): Gtd2Stats {
    return Gtd2Stats(
        timeLeft = calculateTimeLeft(),
        estimate = tasksState.estimate() ?: 0.toSeconds()
    )
}

@SuppressLint("NewApi")
private fun calculateTimeLeft(): TimeValue {
    return (LocalTime.MAX.toSecondOfDay() - LocalTime.now()
        .toSecondOfDay()).let { PositiveSeconds(it) }
}
