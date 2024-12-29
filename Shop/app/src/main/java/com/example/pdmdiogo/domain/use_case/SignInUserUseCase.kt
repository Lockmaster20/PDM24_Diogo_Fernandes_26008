package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.repository.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class SignInUserUseCase (private val repository: AuthRepository) {
    operator fun invoke(email: String, password: String): Task<AuthResult> {
        return repository.signInUser(email, password)
    }
}