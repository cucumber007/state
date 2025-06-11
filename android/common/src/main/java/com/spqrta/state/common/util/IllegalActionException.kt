package com.spqrta.state.common.util

class IllegalActionException(action: Any, state: Any?) :
    IllegalStateException("Illegal action $action for state $state")
