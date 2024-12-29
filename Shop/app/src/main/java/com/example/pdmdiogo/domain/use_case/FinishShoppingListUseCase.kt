package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.repository.ShoppingListRepository

class FinishShoppingListUseCase(private val repository: ShoppingListRepository) {
    operator fun invoke(listId: String, onComplete: (Boolean) -> Unit) {
        return repository.finishShoppingList(listId, onComplete)
    }
}