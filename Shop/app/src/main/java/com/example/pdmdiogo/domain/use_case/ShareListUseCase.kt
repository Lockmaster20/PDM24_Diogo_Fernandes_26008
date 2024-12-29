package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.repository.ShoppingListRepository

class ShareListUseCase(private val repository: ShoppingListRepository) {
    operator fun invoke(listId: String, userId: String, onComplete: (Boolean) -> Unit) {
        return repository.shareList(listId, userId, onComplete)
    }
}