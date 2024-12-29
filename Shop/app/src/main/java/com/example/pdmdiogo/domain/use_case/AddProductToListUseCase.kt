package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.model.Product
import com.example.pdmdiogo.domain.repository.ProductRepository

class AddProductToListUseCase (private val repository: ProductRepository) {
    operator fun invoke(listId: String, productId: String, onComplete: (Boolean) -> Unit) {
        return repository.addProductToList(listId, productId, onComplete)
    }
}