package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.model.ShoppingList
import com.example.pdmdiogo.domain.repository.ShoppingListRepository

class GetShoppingListByIdUseCase(private val repository: ShoppingListRepository) {
    operator fun invoke(listId: String, onResult: (ShoppingList?) -> Unit) {
        return repository.getShoppingListById(listId, onResult)
    }
}