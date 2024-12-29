package com.example.pdmdiogo.data.repository

import android.util.Log
import com.example.pdmdiogo.data.remote.FirestoreSource
import com.example.pdmdiogo.domain.model.User
import com.example.pdmdiogo.domain.repository.UserRepository
import com.google.android.gms.tasks.Task

class UserRepositoryImpl(
    private val firestore: FirestoreSource
) : UserRepository {

    override fun saveUserData(uid:String, name:String, email:String): Task<Boolean> {
        val userData = hashMapOf(
            "name" to name,
            "email" to email
        )
        return firestore.db.collection("users").document(uid)
            .set(userData)
            .continueWith { task ->
                task.isSuccessful
            }
    }

    override fun getUserData(uid: String, onResult: (User?) -> Unit) {
        firestore.db.collection("users").document(uid).get()
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

    override fun getUsers(onResult: (List<User>) -> Unit) {
        firestore.db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val users = result.documents.mapNotNull { it.toObject(User::class.java)?.apply { id = it.id } }
                    onResult(users)
                } else {
                    Log.e("Firestore", "Erro utilizadores não encontrados")
                    onResult(emptyList())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro obter os utilizadores", exception)
                onResult(emptyList())
            }
    }
}

