package com.spqrta.state.common.logic.features.gtd2.meta

import com.spqrta.state.common.logic.features.gtd2.tinder.TinderPrompt
import kotlinx.serialization.Serializable
import java.lang.reflect.Field
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

// environment conditions that affects GTD
@Serializable
data class MetaState(
    val goalFunction: GoalFunctionState,
    val workdayStarted: Boolean,
) {
    val prompts: Collection<TinderPrompt> by lazy {
        getNullProperties(goalFunction).map {
            TinderPrompt.UnknownMetaState(it)
        }
    }

    private fun getNullProperties(instance: Any): List<MetaProperty> {
        return instance::class.memberProperties.map {
            val value = it.call(instance)

            when {
                value == null -> {
                    return@map listOf(MetaProperty(it.name, Type.fromKType(it.returnType)))
                }

                value::class.isData -> {
                    getNullProperties(value)
                }

                else -> {
                    listOf()
                }
            }
        }.flatten()
    }

    fun set(property: MetaProperty, value: Boolean): MetaState {
        val copi = copy()
        walkAndSet(copi, property, value)
        return copi
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun walkAndSet(instance: Any, property: MetaProperty, propertyValue: Boolean) {
        instance::class.memberProperties.map {
            val value = it.call(instance)

            when {
                value == null -> {
                    if (it.name == property.name) {
                        instance::class.java.getDeclaredField(it.name).also { field ->
                            field.isAccessible = true
                        }.set(instance, propertyValue)
                    } else {
                        // do nothing
                    }
                }

                value::class.isData -> {
                    walkAndSet(value, property, propertyValue)
                }

                else -> {
                    // do nothing
                }
            }
        }
    }

    private fun setImmutableProperty(instance: Any, fieldName: String, newValue: Any) {
        val field: Field = instance.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true  // Bypass the private and final modifiers

        val modifiersField: Field = Field::class.java.getDeclaredField("modifiers")
        modifiersField.isAccessible = true
        modifiersField.setInt(field, field.modifiers and java.lang.reflect.Modifier.FINAL.inv())

        field.set(instance, newValue)
    }

    companion object {
        val INITIAL = MetaState(
            workdayStarted = false,
            goalFunction = GoalFunctionState(
                body = BodyState(
                    isTeethClean = null
                )
            )
        )

        val SERVICE_PROPERTIES = listOf(
            MetaState::prompts,
        ).map {
            it as KProperty1<*, *>
        }
    }
}
