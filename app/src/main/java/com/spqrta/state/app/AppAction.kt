package com.spqrta.state.app.state

import java.time.LocalDate


sealed interface AppAction

object InitAppAction: AppAction
data class StateLoadedAction(val state: AppReady): AppAction
data class AppErrorAction(val exception: Exception): AppAction
data class OnResumeAction(val date: LocalDate = LocalDate.now()): AppAction
