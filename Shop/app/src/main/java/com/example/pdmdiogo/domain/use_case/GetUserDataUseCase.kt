package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.model.User
import com.example.pdmdiogo.domain.repository.UserRepository

class GetUserDataUseCase(private val repository: UserRepository) {
    operator fun invoke(uid: String, onResult: (User?) -> Unit) {
        return repository.getUserData(uid, onResult)
    }
}