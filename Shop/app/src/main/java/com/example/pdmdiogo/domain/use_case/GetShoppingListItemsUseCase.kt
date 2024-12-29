package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.model.Product
import com.example.pdmdiogo.domain.repository.ProductRepository

class GetShoppingListItemsUseCase (private val repository: ProductRepository) {
    operator fun invoke(listId: String, onResult: (List<Product>) -> Unit) {
        return repository.getShoppingListItems(listId, onResult)
    }
}