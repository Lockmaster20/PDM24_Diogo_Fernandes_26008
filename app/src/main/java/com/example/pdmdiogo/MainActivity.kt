package com.example.pdmdiogo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pdmdiogo.Calculator.First
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
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        baseContext,
                        "Authentication Success",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // If sign in fails, display a message to the user.
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
            .addOnSuccessListener { documentItems ->
                if (documentItems.exists()) {
                    val user = documentItems.toObject(User::class.java)
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onResult(null)
            }
    }
}

@Preview(showBackground = true)
@Composable
fun TestPreview() {
    First()
}