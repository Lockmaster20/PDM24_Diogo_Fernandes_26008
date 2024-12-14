package com.example.pdmdiogo.Shop.domain.model

data class ShoppingList(
    var id: String = "",
    val ownerId: String = "",
    val name: String = "",
    val isFinished: Boolean = false
)