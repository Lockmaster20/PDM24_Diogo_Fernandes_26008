package com.example.pdmdiogo.data.model

import com.example.pdmdiogo.domain.model.ShoppingList

data class ShoppingListDto(
    val id: String = "",
    val ownerId: String = "",
    val name: String = "",
    val isFinished: Boolean = false,
    val sharedWith: List<String> = emptyList(),
    val items: List<String> = emptyList()
) {
    fun toDomain() = ShoppingList(id = id, ownerId = ownerId, name = name, isFinished = isFinished, sharedWith = sharedWith, items = items)
}