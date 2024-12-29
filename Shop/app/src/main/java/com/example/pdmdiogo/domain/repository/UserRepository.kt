package com.example.pdmdiogo.domain.repository

import com.example.pdmdiogo.domain.model.User
import com.google.android.gms.tasks.Task

interface UserRepository {
    fun saveUserData(uid:String, name:String, email:String): Task<Boolean>
    fun getUserData(uid: String, onResult: (User?) -> Unit)
    fun getUsers(onResult: (List<User>) -> Unit)
}