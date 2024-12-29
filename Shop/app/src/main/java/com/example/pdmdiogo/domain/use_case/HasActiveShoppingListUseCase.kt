package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.repository.ShoppingListRepository

class HasActiveShoppingListUseCase(private val repository: ShoppingListRepository) {
    operator fun invoke(uid: String, onResult: (Boolean) -> Unit) {
        return repository.hasActiveShoppingList(uid, onResult)
    }
}