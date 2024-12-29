package com.example.pdmdiogo.data.model

import com.example.pdmdiogo.domain.model.User

data class UserDto(
    val id: String = "",
    val name: String = "",
    val email: String = ""
) {
    fun toDomain() = User(id = id, name = name, email = email)
}