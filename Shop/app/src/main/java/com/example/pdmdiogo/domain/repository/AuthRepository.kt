package com.example.pdmdiogo.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface AuthRepository {
    fun registerUser(email: String, password: String): Task<AuthResult>
    fun signInUser(email: String, password: String): Task<AuthResult>
    fun signOut()
}