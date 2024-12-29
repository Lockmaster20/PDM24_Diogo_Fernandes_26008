package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.model.Product
import com.example.pdmdiogo.domain.repository.ProductRepository

class GetProductUseCase(private val repository: ProductRepository) {
    operator fun invoke(onResult: (List<Product>) -> Unit) {
        return repository.getProducts(onResult)
    }
}