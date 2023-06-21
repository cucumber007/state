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
        TodoItem("Зубы Утро"),
        TodoItem("Вода кровать", "чтобы не ебаться ночью"),
        TodoItem("Запарить еду"),
        TodoItem("Таблы Утро"),
        TodoItem("Завтрак"),
        TodoItem("Колл", "для денег"),
        TodoItem("Чек: --"),
        TodoItem("Посуда", "чтобы было из чего есть"),
        TodoItem("Кувшин", "чтобы было что пить"),
        TodoItem("Бритье", "чтобы было красиво"),
        TodoItem("Дегтярное мыло", "чтобы не болеть"),
        TodoItem("Стол", "чтобы было где есть"),
        TodoItem("Кухня", "для морального удовлетворения от чистоты"),
        TodoItem("Убрать", "чтобы не муляло"),
        TodoItem("----"),
        TodoItem("Крем", "для комфорта"),
        TodoItem("Сорт", "для душевного покоя"),
        TodoItem("Таски", "чтобы не забыть"),
        TodoItem("Зарядка", "чтобы не болеть"),
        TodoItem("Запасы", "чтобы потом не бегать"),
        TodoItem("Подготовить еду", "чтобы не голодать"),
        TodoItem("Таблы", "чтобы не болеть"),
        TodoItem("----"),
        TodoItem("Зубы Вечер", "чтобы ночью там ничего не размножалось"),
        TodoItem("Таблы Вечер", "чтобы не болеть"),
        TodoItem("Таски", "чтобы закончить день"),
        TodoItem("Кл чек", "чтобы вовремя спохватиться"),
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
