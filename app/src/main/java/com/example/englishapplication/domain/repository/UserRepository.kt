package com.example.englishapplication.domain.repository

import com.example.englishapplication.domain.model.CreateUserRequest
import com.example.englishapplication.domain.model.LoginRequest
import retrofit2.Response

interface UserRepository {
    suspend fun login(loginRequest: LoginRequest): Result<Unit>
    suspend fun createUser(createUserRequest: CreateUserRequest): Result<Unit>
}