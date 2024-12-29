package com.example.pdmdiogo.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdmdiogo.data.remote.FirebaseSource
import com.example.pdmdiogo.data.repository.AuthRepositoryImpl
import com.example.pdmdiogo.domain.use_case.RegisterUserUseCase
import com.example.pdmdiogo.domain.use_case.SignInUserUseCase
import com.example.pdmdiogo.domain.use_case.SignOutUseCase
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val firebaseSource = FirebaseSource()
    private val authRepository = AuthRepositoryImpl(firebaseSource)
    private val registerUserUseCase = RegisterUserUseCase(authRepository)
    private val signInUserUseCase = SignInUserUseCase(authRepository)
    private val signOutUseCase = SignOutUseCase(authRepository)

    var authState by mutableStateOf<Boolean?>(null)
    var registerState by mutableStateOf<Boolean?>(null)
    var user by mutableStateOf<FirebaseUser?>(null)

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            registerUserUseCase(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    registerState = true
                    user = task.result?.user
                }
            }
        }
    }

    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            signInUserUseCase(email, password).addOnCompleteListener { task ->
                authState = task.isSuccessful
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
            user = null
        }
    }
}