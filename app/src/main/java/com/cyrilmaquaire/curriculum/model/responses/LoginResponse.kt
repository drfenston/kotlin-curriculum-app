package com.cyrilmaquaire.curriculum.model.responses

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val data: User,
    val token: String,
) : Response() {

    @Serializable
    data class User(
        val id: Long,
        val username: String,
        val password: String,
        val createdAt: String,
        val updatedAt: String,
    )
}