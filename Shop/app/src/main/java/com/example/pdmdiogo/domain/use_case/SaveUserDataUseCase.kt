package com.example.pdmdiogo.domain.use_case

import com.example.pdmdiogo.domain.repository.UserRepository
import com.google.android.gms.tasks.Task

class SaveUserDataUseCase(private val repository: UserRepository) {
    operator fun invoke(uid:String, name:String, email:String): Task<Boolean> {
        return repository.saveUserData(uid, name, email)
    }
}