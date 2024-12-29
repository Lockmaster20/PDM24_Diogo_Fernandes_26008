package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.model.ShoppingList
import com.example.pdmdiogo.domain.repository.ShoppingListRepository

class GetSharedShoppingListUseCase(private val repository: ShoppingListRepository) {
    operator fun invoke(uid: String, onResult: (List<ShoppingList>) -> Unit) {
        return repository.getSharedShoppingLists(uid, onResult)
    }
}