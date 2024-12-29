package com.example.pdmdiogo.domain.repository

import com.example.pdmdiogo.domain.model.ShoppingList

interface ShoppingListRepository {
    fun createShoppingList(name: String, ownerId: String, onComplete: (Boolean) -> Unit)
    fun shareList(listId: String, userId: String, onComplete: (Boolean) -> Unit)
    fun getShoppingList(uid: String, onResult: (List<ShoppingList>) -> Unit)
    fun getShoppingListById(listId: String, onResult: (ShoppingList?) -> Unit)
    fun getActiveShoppingList(uid: String, onResult: (ShoppingList?) -> Unit)
    fun getSharedShoppingLists(uid: String, onResult: (List<ShoppingList>) -> Unit)
    fun hasActiveShoppingList(uid: String, onResult: (Boolean) -> Unit)
    fun finishShoppingList(listId: String, onComplete: (Boolean) -> Unit)
}