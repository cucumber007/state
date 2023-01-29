package com.spqrta.state.app.state

import java.time.LocalDateTime


sealed interface AppAction

sealed class AppGlobalAction: AppAction {
    override fun toString(): String = javaClass.simpleName
}
object InitAppAction: AppGlobalAction()
data class StateLoadedAction(val state: AppReady): AppGlobalAction()
data class AppErrorAction(val exception: Exception): AppGlobalAction()
data class OnResumeAction(val datetime: LocalDateTime = LocalDateTime.now()): AppGlobalAction()
