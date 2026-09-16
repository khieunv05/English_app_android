package com.example.englishapplication.domain.model

data class LoginResponse(
    val token: String,
    val refreshToken: String,
    val username: String
)