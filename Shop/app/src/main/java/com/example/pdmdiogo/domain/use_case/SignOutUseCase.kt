package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.repository.AuthRepository

class SignOutUseCase (private val repository: AuthRepository) {
    operator fun invoke() {
        return repository.signOut()
    }
}