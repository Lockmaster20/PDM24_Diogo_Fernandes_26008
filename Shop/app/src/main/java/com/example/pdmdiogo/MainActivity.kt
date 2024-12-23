package com.example.pdmdiogo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pdmdiogo.Shop.domain.model.Product
import com.example.pdmdiogo.Shop.domain.model.ShoppingList
import com.example.pdmdiogo.Shop.domain.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
        setContent {
            Shop()
        }
    }

    fun registerUserWithFirebase(email:String, password:String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Registration Success",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Registration Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun signInUserWithFirebase(email:String, password:String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Authentication Success",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    fun saveUserData(uid:String, name:String, email:String): Task<Boolean> {
        val userData = hashMapOf(
            "name" to name,
            "email" to email
        )
        return db.collection("users").document(uid)
            .set(userData)
            .continueWith { task ->
                task.isSuccessful
            }
    }

    fun getUserData(uid: String, onResult: (User?) -> Unit) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }


    fun createShoppingList(title: String, ownerId: String, onComplete: (Boolean) -> Unit) {
        val shoppingList = hashMapOf(
            "title" to title,
            "isFinished" to false,
            "ownerId" to ownerId
        )

        db.collection("shopping_lists")
            .add(shoppingList)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun addProductToList(listId: String, productId: String, onComplete: (Boolean) -> Unit) {
        db.collection("shopping_lists")
            .document(listId)
            .update("items", FieldValue.arrayUnion(productId))
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun shareList(listId: String, userId: String, onComplete: (Boolean) -> Unit) {
        db.collection("shopping_lists")
            .document(listId)
            .update("sharedWith", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun getShoppingList(uid: String, onResult: (List<ShoppingList>) -> Unit) {
        db.collection("shopping_lists").whereEqualTo("ownerId", uid).get()
            .addOnSuccessListener { result ->
                val lists = result.documents.mapNotNull { it.toObject(ShoppingList::class.java)?.apply { id = it.id } }
                onResult(lists)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun getShoppingListById(listId: String, onResult: (ShoppingList?) -> Unit) {
        db.collection("shopping_lists")
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

    fun getActiveShoppingList(uid: String, onResult: (ShoppingList?) -> Unit) {
        db.collection("shopping_lists")
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

    fun getSharedShoppingLists(uid: String, onResult: (List<ShoppingList>) -> Unit) {
        db.collection("shopping_lists")
            .whereArrayContains("sharedWith", uid)
            .whereEqualTo("isFinished", false)
            .get()
            .addOnSuccessListener { result ->
                val lists = result.documents.mapNotNull { it.toObject(ShoppingList::class.java)?.apply { id = it.id } }
                onResult(lists)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun hasActiveShoppingList(uid: String, onResult: (Boolean) -> Unit) {
        db.collection("shopping_lists")
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

    fun finishShoppingList(listId: String, onComplete: (Boolean) -> Unit) {
        db.collection("shopping_lists").document(listId).update("isFinished", true)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getShoppingListItems(listId: String, onResult: (List<Product>) -> Unit) {
        db.collection("shopping_lists")
            .document(listId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val shoppingList = document.toObject(ShoppingList::class.java)
                    if (shoppingList != null) {
                        val productIds = shoppingList.items
                        if (productIds.isNotEmpty()) {
                            db.collection("products")
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

    fun getProducts(onResult: (List<Product>) -> Unit) {
        db.collection("products")
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
                Log.e("Firestore", "Erro obter a produtos", exception)
                onResult(emptyList())
            }
    }

    fun removeItemFromList(listId: String, productId: String, onResult: (Boolean) -> Unit){
        db.collection("shopping_lists")
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
}

@Preview(showBackground = true)
@Composable
fun TestPreview() {
    //First()
}