package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable

@Serializable
sealed interface Element {
    val name: ElementName
    val displayName: String
    val active: Boolean
    fun estimate(): TimeValue?
    fun getElement(name: ElementName): Element?
    fun getToBeDone(name: ElementName): ToBeDone?

    // is Task or Routine with Task
    fun isLeaf(): Boolean

    // is Queue or Flipper that contains only leafs
    fun isLeafGroup(): Boolean

    fun map(mapper: (Element) -> Element): Element

    fun groups(): List<Group>
    fun nonEstimated(): List<Element>
    fun tasks(): List<Task>
    fun toBeDone(): List<ToBeDone>

    // update active status of the element
    fun withActive(active: Boolean): Element

    // make all done tasks active (inactive are intact)
    fun withDoneReset(): Element

    fun withElement(name: ElementName, action: (element: Element) -> Element): Element
    fun withEstimate(name: ElementName, estimate: TimeValue?): Element
    fun withNewContext(metaState: MetaState): Element
}

fun Element.withTask(name: ElementName.TaskName, action: (task: Task) -> Task): Element {
    return withElement(name) {
        if (it is Task) {
            action(it)
        } else {
            it
        }
    }
}

fun Element.withTask(task: Task, action: (task: Task) -> Task): Element {
    return withTask(task.name, action)
}

fun Element.withToBeDone(name: ElementName, action: (toBeDone: ToBeDone) -> ToBeDone): Element {
    return withElement(name) {
        if (it is ToBeDone) {
            action(it) as Element
        } else {
            it
        }
    }
}

fun Element.withToBeDone(toBeDone: ToBeDone, action: (toBeDone: ToBeDone) -> ToBeDone): Element {
    return withToBeDone(toBeDone.name, action)
}
