package com.example.pdmdiogo.data.model

import com.example.pdmdiogo.domain.model.Product

data class ProductDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0
) {
    fun toDomain() = Product(id = id, name = name, description = description, price = price)
}