package com.example.pdmdiogo.data.repository

import com.example.pdmdiogo.data.remote.FirebaseSource
import com.example.pdmdiogo.domain.repository.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class AuthRepositoryImpl(
    private val firebaseSource: FirebaseSource
) : AuthRepository {

    override fun registerUser(email: String, password: String): Task<AuthResult> {
        return firebaseSource.auth.createUserWithEmailAndPassword(email, password)
    }

    override fun signInUser(email: String, password: String): Task<AuthResult> {
        return firebaseSource.auth.signInWithEmailAndPassword(email, password)
    }

    override fun signOut() {
        firebaseSource.auth.signOut()
    }
}