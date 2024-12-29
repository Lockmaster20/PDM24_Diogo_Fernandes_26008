package com.example.pdmdiogo.data.repository

import android.util.Log
import com.example.pdmdiogo.data.remote.FirestoreSource
import com.example.pdmdiogo.domain.model.Product
import com.example.pdmdiogo.domain.model.ShoppingList
import com.example.pdmdiogo.domain.repository.ProductRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue

class ProductRepositoryImpl(
    private val firestore: FirestoreSource
) : ProductRepository {

    override fun getShoppingListItems(listId: String, onResult: (List<Product>) -> Unit) {
        firestore.db.collection("shopping_lists")
            .document(listId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val shoppingList = document.toObject(ShoppingList::class.java)
                    if (shoppingList != null) {
                        val productIds = shoppingList.items
                        if (productIds.isNotEmpty()) {
                            firestore.db.collection("products")
                                .whereIn(FieldPath.documentId(), productIds)
                                .get()
                                .addOnSuccessListener { results ->
                                    val listItems = results.documents.mapNotNull { doc ->
                                        val product = doc.toObject(Product::class.java)
                                        product?.let {
                                            Product(
                                                id = doc.id,
                                                name = it.name,
                                                description = it.description,
                                                price = it.price
                                            )
                                        }
                                    }
                                    onResult(listItems)
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("Firestore", "Erro a obter produtos", exception)
                                    onResult(emptyList())
                                }
                        } else {
                            onResult(emptyList()) // Lista sem produtos
                        }
                    } else {
                        onResult(emptyList()) // Lista não encontrada
                    }
                } else {
                    onResult(emptyList()) // Lista não encontrada
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro a obter lista", exception)
                onResult(emptyList())
            }
    }

    override fun addProductToList(listId: String, productId: String, onComplete: (Boolean) -> Unit) {
        firestore.db.collection("shopping_lists")
            .document(listId)
            .update("items", FieldValue.arrayUnion(productId))
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    override fun removeItemFromList(listId: String, productId: String, onResult: (Boolean) -> Unit){
        firestore.db.collection("shopping_lists")
            .document(listId)
            .update("items", FieldValue.arrayRemove(productId))
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro a remover o produto da lista", exception)
                onResult(false)
            }
    }

    override fun getProducts(onResult: (List<Product>) -> Unit) {
        firestore.db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val products = result.documents.mapNotNull { it.toObject(Product::class.java)?.apply { id = it.id } }
                    onResult(products)
                } else {
                    Log.e("Firestore", "Erro produtos não encontrados")
                    onResult(emptyList())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro obter os produtos", exception)
                onResult(emptyList())
            }
    }
}