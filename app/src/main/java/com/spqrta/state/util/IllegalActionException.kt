package com.spqrta.state.util

class IllegalActionException(action: Any, state: Any?) :
    IllegalStateException("Illegal action $action for state $state")
