package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.model.User
import com.example.pdmdiogo.domain.repository.UserRepository

class GetUsersUseCase(private val repository: UserRepository) {
    operator fun invoke(onResult: (List<User>) -> Unit) {
        return repository.getUsers(onResult)
    }
}