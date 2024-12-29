package com.example.pdmdiogo.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdmdiogo.data.remote.FirestoreSource
import com.example.pdmdiogo.data.repository.ProductRepositoryImpl
import com.example.pdmdiogo.data.repository.ShoppingListRepositoryImpl
import com.example.pdmdiogo.domain.model.Product
import com.example.pdmdiogo.domain.model.ShoppingList
import com.example.pdmdiogo.domain.use_case.AddProductToListUseCase
import com.example.pdmdiogo.domain.use_case.CreateShoppingListUseCase
import com.example.pdmdiogo.domain.use_case.FinishShoppingListUseCase
import com.example.pdmdiogo.domain.use_case.GetActiveShoppingListUseCase
import com.example.pdmdiogo.domain.use_case.GetSharedShoppingListUseCase
import com.example.pdmdiogo.domain.use_case.GetShoppingListByIdUseCase
import com.example.pdmdiogo.domain.use_case.GetShoppingListItemsUseCase
import com.example.pdmdiogo.domain.use_case.RemoveItemFromListUseCase
import com.example.pdmdiogo.domain.use_case.ShareListUseCase
import kotlinx.coroutines.launch


class ShoppingListViewModel : ViewModel() {

    private val firestoreSource = FirestoreSource()
    private val shoppingListRepository = ShoppingListRepositoryImpl(firestoreSource)
    private val productRepository = ProductRepositoryImpl(firestoreSource)
    private val getActiveShoppingListUseCase = GetActiveShoppingListUseCase(shoppingListRepository)
    private val getSharedShoppingListUseCase = GetSharedShoppingListUseCase(shoppingListRepository)
    private val getShoppingListByIdUseCase = GetShoppingListByIdUseCase(shoppingListRepository)
    private val getShoppingListItemsUseCase = GetShoppingListItemsUseCase(productRepository)
    private val addProductToListUseCase = AddProductToListUseCase(productRepository)
    private val removeItemFromListUseCase = RemoveItemFromListUseCase(productRepository)
    private val shareListUseCase = ShareListUseCase(shoppingListRepository)
    private val finishShoppingListUseCase = FinishShoppingListUseCase(shoppingListRepository)
    private val createShoppingListUseCase = CreateShoppingListUseCase(shoppingListRepository)

    var activeShoppingList by mutableStateOf<ShoppingList?>(null)

    var hasActiveList by mutableStateOf(false)

    var sharedLists by mutableStateOf<List<ShoppingList>>(emptyList())

    var listItems by mutableStateOf<List<Product>>(emptyList())

    var shoppingList by mutableStateOf<ShoppingList?>(null)

    fun getActiveShoppingList(uid: String) {
        viewModelScope.launch {
            getActiveShoppingListUseCase(uid) { list ->
                activeShoppingList = list
                hasActiveList = list != null
            }
        }
    }

    fun getShoppingListById(listId: String) {
        viewModelScope.launch {
            getShoppingListByIdUseCase(listId) { list ->
                shoppingList = list
            }
        }
    }

    fun getShoppingListItems(listId: String) {
        viewModelScope.launch {
            getShoppingListItemsUseCase(listId) { items ->
                listItems = items
            }
        }
    }

    fun addProductToList(listId: String, productId: String, onComplete: (Boolean) -> Unit) {
        addProductToListUseCase(listId, productId) { success ->
            if (success) {
                getShoppingListItems(listId)
            }
            onComplete(success)
        }
    }

    fun removeItemFromList(listId: String, productId: String) {
        removeItemFromListUseCase(listId, productId) { success ->
            if (success) {
                getShoppingListItems(listId)
            }
        }
    }

    fun shareList(listId: String, userId: String, onComplete: (Boolean) -> Unit) {
        shareListUseCase(listId, userId) { success ->
            onComplete(success)
        }
    }

    fun getSharedShoppingLists(userId: String) {
        viewModelScope.launch {
            getSharedShoppingListUseCase(userId) { lists ->
                sharedLists = lists
            }
        }
    }

    fun finishShoppingList(listId: String, onComplete: (Boolean) -> Unit) {
        finishShoppingListUseCase(listId) { completed ->
            onComplete(completed)
        }
    }

    fun createShoppingList(name: String, userId: String) {
        viewModelScope.launch {
            createShoppingListUseCase(name, userId) { success ->
                if (success) {
                    getActiveShoppingList(userId)
                }
            }
        }
    }
}