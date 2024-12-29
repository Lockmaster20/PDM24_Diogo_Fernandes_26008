package com.example.pdmdiogo.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdmdiogo.data.remote.FirestoreSource
import com.example.pdmdiogo.data.repository.ProductRepositoryImpl
import com.example.pdmdiogo.domain.model.Product
import com.example.pdmdiogo.domain.use_case.GetProductUseCase
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val firestoreSource = FirestoreSource()
    private val productRepository = ProductRepositoryImpl(firestoreSource)
    private val getProductUseCase = GetProductUseCase(productRepository)

    var productsList by mutableStateOf<List<Product>>(emptyList())

    fun loadProducts() {
        viewModelScope.launch {
            getProductUseCase { products ->
                productsList = products
            }
        }
    }
}