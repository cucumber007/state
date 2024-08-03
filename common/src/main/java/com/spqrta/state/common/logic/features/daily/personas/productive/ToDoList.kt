package com.spqrta.state.common.logic.features.daily.personas.productive

import com.spqrta.state.common.logic.action.ToDoListAction
import com.spqrta.state.common.logic.features.global.AppEffectLegacy
import com.spqrta.state.common.logic.features.global.VibrateEffect
import com.spqrta.state.common.util.optics.OpticOptional
import com.spqrta.state.common.util.optics.asOpticOptional
import com.spqrta.state.common.util.optics.wrap
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.withEffects
import kotlinx.serialization.Serializable

@Serializable
data class ToDoList(

    @Suppress("SpellCheckingInspection")
    val items: List<TodoItem> = listOf(
        TodoItem("💊 Экомед"),
        TodoItem("🪥 Зубы Утро"),
        TodoItem("🥛 Вода кровать"),
        TodoItem("🍜 Запарить еду"),
        TodoItem("🪟 Проветривание утро"),
        TodoItem("🥬 Растения утро"),
        TodoItem("💧 Увлажнитель утро"),
        TodoItem("💊 Таблы утро"),
        TodoItem("🍳 Завтрак"),
        TodoItem("📳 Проверить работу"),
        TodoItem("📅 Calendar"),
        TodoItem("🧘‍ Медитация утро", "не забудь про цель медитации"),
        TodoItem("👟 Зарядка базовая", "Чтобы были норм ощущения, восстановить гомеостаз"),
        TodoItem("💧 Кувшин"),
        TodoItem("🪒 Бритье"),
        TodoItem("🫧 Дегтярное мыло"),
        TodoItem("🚿 Душ", "Чтобы было ощущение чистоты и бодрости"),
        TodoItem("🧴 Крем"),
        TodoItem("🍽️ Посуда"),
        TodoItem("🥘 Кухня Убрать"),
        TodoItem("🧹 Убрать / пылесос"),
        TodoItem("🧠 Сорт + Incomed (15 минут)"),
        TodoItem("📊 Таски (15 минут + cорт)"),
        TodoItem("💪 Зарядка", "Чтобы расти"),
        TodoItem("🧘‍ Медитация стартовая"),
        TodoItem("💊 Таблы обед"),
        TodoItem("🍔 Обед"),
        TodoItem("👷‍ Работа"),
        TodoItem("📦 Запасы"),
        TodoItem("🥡 Еда Чеклист"),
        TodoItem("🏌️ Fucking walk"),
        TodoItem("🪟 Проветривание вечер"),
        TodoItem("💊 Таблы вечер"),
        TodoItem("🥬 Растения вечер"),
        TodoItem("🧘‍ Медитация вечер", "не забудь про цель медитации"),
        TodoItem("🪥 Зубы вечер"),
        TodoItem("💧 Увлажнитель вечер"),
    )

) {
    companion object {
        fun reduce(
            action: ToDoListAction,
            state: ToDoList
        ): Reduced<out ToDoList, out AppEffectLegacy> {
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
