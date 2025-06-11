package com.spqrta.state.common.util.debug

import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName

fun Element.debugFirstTask(): Task {
    return getToBeDone(ElementName.TaskName("Brush my teeth")) as Task
}

fun Element.debugAlsoFirstTask(block: (Task) -> Unit): Element {
    block(debugFirstTask())
    return this
}
