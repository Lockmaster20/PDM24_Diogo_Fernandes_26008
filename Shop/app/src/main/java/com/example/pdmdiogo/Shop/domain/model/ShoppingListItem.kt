package com.example.pdmdiogo.Shop.domain.model

data class ShoppingListItem(
    val name: String = "",
    val quantity: Int = 0,
    val isChecked: Boolean = false
)