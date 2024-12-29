package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.repository.ShoppingListRepository

class CreateShoppingListUseCase(private val repository: ShoppingListRepository) {
    operator fun invoke(name: String, ownerId: String, onComplete: (Boolean) -> Unit) {
        return repository.createShoppingList(name, ownerId, onComplete)
    }
}