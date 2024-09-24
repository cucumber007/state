package com.spqrta.state.common

import com.spqrta.state.common.util.testLog


fun wrapLog(code: () -> Unit) {
    testLog("\n\n===============================")
    code()
    testLog("\n===============================\n\n")
}
