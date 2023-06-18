package com.spqrta.state.app.features.daily.personas.productive

import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.PlayNotificationSoundEffect
import com.spqrta.state.app.VibrateEffect
import com.spqrta.state.app.action.ToDoListAction
import com.spqrta.state.util.optics.OpticOptional
import com.spqrta.state.util.optics.asOpticOptional
import com.spqrta.state.util.optics.wrap
import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.withEffects
import kotlinx.serialization.Serializable

@Serializable
data class ToDoList(
    val items: List<TodoItem> = listOf(
        TodoItem("Зубы"),
        TodoItem("Вода кровать"),
        TodoItem("Лактулоза"),
        TodoItem("Запарить еду"),
        TodoItem("Таблы"),
        TodoItem("Завтрак"),
        TodoItem("Колл"),
        TodoItem("Чек: --"),
        TodoItem("Посуда"),
        TodoItem("Кувшин"),
        TodoItem("Бритье"),
        TodoItem("Дегтярное мыло"),
        TodoItem("Стол"),
        TodoItem("Кухня"),
        TodoItem("Убрать"),
        TodoItem("----"),
        TodoItem("Крем"),
        TodoItem("Сорт"),
        TodoItem("Таски"),
        TodoItem("Зарядка"),
        TodoItem("Запасы"),
        TodoItem("Подготовить еду"),
    )
) {
    companion object {
        fun reduce(
            action: ToDoListAction,
            state: ToDoList
        ): Reduced<out ToDoList, out AppEffect> {
            return when (action) {
                is ToDoListAction.OnPress -> {
                    wrap(state, optItem(action.title)) { item ->
                        item.copy(checked = !item.checked).withEffects(
                            VibrateEffect
                        )
                    }
                }
            }
        }

        fun optItem(name: String): OpticOptional<ToDoList, TodoItem> {
            return ({ state: ToDoList ->
                state.items.find { it.title == name }
            } to
                    { state: ToDoList, subState: TodoItem ->
                        state.copy(
                            items = state.items.map {
                                if (it.title == name) {
                                    subState
                                } else {
                                    it
                                }
                            }
                        )
                    }).asOpticOptional()
        }
    }
}
