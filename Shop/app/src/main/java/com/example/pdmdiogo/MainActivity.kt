package com.example.pdmdiogo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pdmdiogo.Calculator.First
import com.example.pdmdiogo.Shop.domain.model.ShoppingList
import com.example.pdmdiogo.Shop.domain.model.ShoppingListItem
import com.example.pdmdiogo.Shop.domain.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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
            //First()   //Calculadora
            //MainScreen()  //Notícias
            Shop() //Firebase
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

    fun createShoppingList(uid: String, name: String, onComplete: (Boolean) -> Unit) {
        val listData = mapOf(
            "ownerId" to uid,
            "name" to name,
            "isFinished" to false
        )
        db.collection("shopping_lists").add(listData)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // !!! Mudar para só carregar listas não terminadas?
    fun getShoppingLists(uid: String, onResult: (List<ShoppingList>) -> Unit) {
        db.collection("shopping_lists").whereEqualTo("ownerId", uid).get()
            .addOnSuccessListener { result ->
                val lists = result.documents.mapNotNull { it.toObject(ShoppingList::class.java)?.apply { id = it.id } }
                onResult(lists)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun getSharedShoppingLists(uid: String, onResult: (List<ShoppingList>) -> Unit) {
        db.collection("shopping_lists").whereArrayContains("sharedWith", uid).get()
            .addOnSuccessListener { result ->
                val lists = result.documents.mapNotNull { it.toObject(ShoppingList::class.java)?.apply { id = it.id } }
                onResult(lists)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun finishShoppingList(listId: String, onComplete: (Boolean) -> Unit) {
        db.collection("shopping_lists").document(listId).update("isFinished", true)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getShoppingListItems(listId: String, onResult: (List<ShoppingListItem>) -> Unit) {
        db.collection("shopping_lists").document(listId).collection("items").get()
            .addOnSuccessListener { result ->
                val items = result.documents.mapNotNull { it.toObject(ShoppingListItem::class.java) }
                onResult(items)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun addShoppingListItem(listId: String, item: ShoppingListItem, onComplete: (Boolean) -> Unit) {
        db.collection("shopping_lists").document(listId).collection("items").add(item)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}

@Preview(showBackground = true)
@Composable
fun TestPreview() {
    First()
}