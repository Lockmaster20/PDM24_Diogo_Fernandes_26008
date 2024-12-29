package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.model.ShoppingList
import com.example.pdmdiogo.domain.repository.ShoppingListRepository

class GetActiveShoppingListUseCase(private val repository: ShoppingListRepository) {
    operator fun invoke(uid: String, onResult: (ShoppingList?) -> Unit) {
        return repository.getActiveShoppingList(uid, onResult)
    }
}