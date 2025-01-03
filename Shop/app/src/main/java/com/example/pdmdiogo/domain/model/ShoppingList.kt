package com.example.pdmdiogo.domain.model

data class ShoppingList(
    var id: String = "",
    val ownerId: String = "",
    val name: String = "",
    val isFinished: Boolean = false,
    val sharedWith: List<String> = emptyList(),
    val items: List<String> = emptyList()
)