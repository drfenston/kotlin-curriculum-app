package com.cyrilmaquaire.curriculum.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
        val username: String,
        val password: String
)