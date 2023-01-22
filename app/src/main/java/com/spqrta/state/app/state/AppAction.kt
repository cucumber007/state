package com.spqrta.state.app.state


sealed interface AppAction

object InitAppAction: AppAction
data class StateLoadedAction(val state: AppReady): AppAction
data class AppErrorAction(val exception: Exception): AppAction
