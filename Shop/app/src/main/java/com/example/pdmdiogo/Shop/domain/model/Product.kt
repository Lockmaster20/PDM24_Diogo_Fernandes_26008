package com.example.pdmdiogo.Shop.domain.model

data class Product(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Number = 0
)