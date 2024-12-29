package com.example.pdmdiogo.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdmdiogo.data.remote.FirestoreSource
import com.example.pdmdiogo.data.repository.UserRepositoryImpl
import com.example.pdmdiogo.domain.model.User
import com.example.pdmdiogo.domain.use_case.GetUsersUseCase
import com.example.pdmdiogo.domain.use_case.SaveUserDataUseCase
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val firestoreSource = FirestoreSource()
    private val userRepository = UserRepositoryImpl(firestoreSource)
    private val saveUserDataUseCase = SaveUserDataUseCase(userRepository)
    private val getUsersUseCase = GetUsersUseCase(userRepository)

    var usersList by mutableStateOf<List<User>>(emptyList())
        private set

    fun saveUserData(uid: String, name: String, email: String) {
        viewModelScope.launch {
            saveUserDataUseCase(uid, name, email).addOnCompleteListener {
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            getUsersUseCase { users ->
                usersList = users
            }
        }
    }
}