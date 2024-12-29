package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.repository.ProductRepository

class RemoveItemFromListUseCase (private val repository: ProductRepository) {
    operator fun invoke(listId: String, productId: String, onResult: (Boolean) -> Unit) {
        return repository.removeItemFromList(listId, productId, onResult)
    }
}