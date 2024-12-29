package com.example.pdmdiogo.domain.repository

import com.example.pdmdiogo.domain.model.Product

interface ProductRepository {
    fun getShoppingListItems(listId: String, onResult: (List<Product>) -> Unit)
    fun addProductToList(listId: String, productId: String, onComplete: (Boolean) -> Unit)
    fun removeItemFromList(listId: String, productId: String, onResult: (Boolean) -> Unit)
    fun getProducts(onResult: (List<Product>) -> Unit)
}