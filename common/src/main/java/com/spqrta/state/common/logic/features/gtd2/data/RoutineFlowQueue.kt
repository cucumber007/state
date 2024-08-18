package com.spqrta.state.common.logic.features.gtd2.data

import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task

object RoutineFlowQueue {
    val value = Routine(
        Queue(
            "Morning",
            listOf(
                Task("Brush my teeth", 5),
                Task("Make my bed", 3),
                Task("Pills", 4),
                Task("Shave", 5),
                Task("Tar soap", 5),
                Task("Take a shower", 15),
                Task("Skincare", 5),
                Task("Get some sunlight", 45),
                Task("Write down what I'm grateful for", 15),
                Task("Meditate", 10),
                Task("Write down goals", 15),
                Task("Clean", 15),
                Task("Make breakfast", 15),
                Task("Breakfast", 15),
                Task("Check calendar", 10),
                Task("Setup ToDo", 10),
                Task("Hold up fruits", 5),
                Task("Exercise", 45),
                Task("Driver license", 15),
                Task("Fill up the jugs", 5),
                Task("Sort tasks", 15),
                Task("Do one task", 15),
                Task("OSP", 15),
                Task("Check storage", 15),
                Task("Food checklist", 30),
                Task("Learn", 15),
                Task("Pre-work meditation", 5),
            )
        )
    )
}
