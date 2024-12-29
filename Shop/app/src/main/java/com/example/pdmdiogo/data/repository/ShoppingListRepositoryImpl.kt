package com.example.pdmdiogo.data.repository

import android.util.Log
import com.example.pdmdiogo.data.remote.FirestoreSource
import com.example.pdmdiogo.domain.model.ShoppingList
import com.example.pdmdiogo.domain.repository.ShoppingListRepository
import com.google.firebase.firestore.FieldValue

class ShoppingListRepositoryImpl(
    private val firestore: FirestoreSource
) : ShoppingListRepository {

    override fun createShoppingList(name: String, ownerId: String, onComplete: (Boolean) -> Unit) {
        val shoppingList = hashMapOf(
            "name" to name,
            "isFinished" to false,
            "ownerId" to ownerId
        )

        firestore.db.collection("shopping_lists")
            .add(shoppingList)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    override fun shareList(listId: String, userId: String, onComplete: (Boolean) -> Unit) {
        firestore.db.collection("shopping_lists")
            .document(listId)
            .update("sharedWith", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    override fun getShoppingList(uid: String, onResult: (List<ShoppingList>) -> Unit) {
        firestore.db.collection("shopping_lists").whereEqualTo("ownerId", uid).get()
            .addOnSuccessListener { result ->
                val lists = result.documents.mapNotNull { it.toObject(ShoppingList::class.java)?.apply { id = it.id } }
                onResult(lists)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    override fun getShoppingListById(listId: String, onResult: (ShoppingList?) -> Unit) {
        firestore.db.collection("shopping_lists")
            .document(listId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val shoppingList = document.toObject(ShoppingList::class.java)?.apply { id = document.id }
                    onResult(shoppingList)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro obter lista", exception)
                onResult(null)
            }
    }

    override fun getActiveShoppingList(uid: String, onResult: (ShoppingList?) -> Unit) {
        firestore.db.collection("shopping_lists")
            .whereEqualTo("ownerId", uid)
            .whereEqualTo("isFinished", false)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    val shoppingList = document.toObject(ShoppingList::class.java)?.apply { id = document.id }
                    onResult(shoppingList)
                } else {
                    Log.e("Firestore", "Erro lista não encontrada")
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro obter lista", exception)
                onResult(null)
            }
    }

    override fun getSharedShoppingLists(uid: String, onResult: (List<ShoppingList>) -> Unit) {
        firestore.db.collection("shopping_lists")
            .whereArrayContains("sharedWith", uid)
            .whereEqualTo("isFinished", false)
            .get()
            .addOnSuccessListener { result ->
                val lists = result.documents.mapNotNull { it.toObject(ShoppingList::class.java)?.apply { id = it.id } }
                onResult(lists)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    override fun hasActiveShoppingList(uid: String, onResult: (Boolean) -> Unit) {
        firestore.db.collection("shopping_lists")
            .whereEqualTo("ownerId", uid)
            .whereEqualTo("isFinished", false)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                onResult(!querySnapshot.isEmpty)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro obter lista", exception)
                onResult(false)
            }
    }

    override fun finishShoppingList(listId: String, onComplete: (Boolean) -> Unit) {
        firestore.db.collection("shopping_lists").document(listId).update("isFinished", true)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}