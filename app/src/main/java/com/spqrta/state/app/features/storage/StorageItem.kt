package com.spqrta.state.app.features.storage

import kotlinx.serialization.Serializable

interface StorageItem {
    val amount: Int
    val normal: Int
    val reverse: Boolean
        get() = false

    open fun isOk(): Boolean {
        return amount >= normal
    }

    val name: String
        get() = javaClass.simpleName


}

sealed class StorageItemOption: StorageItem {
    override fun toString(): String {
        return "${javaClass.simpleName}(amount=$amount, normal=$normal, reverse=$reverse)}"
    }
}

@Serializable
data class CaloryPacks(
    override val amount: Int
) : StorageItemOption() {
    override val normal: Int = 0
}
