package com.example.englishapplication.data.repository

import com.example.englishapplication.data.remote.UserApiService
import com.example.englishapplication.domain.model.CreateUserRequest
import com.example.englishapplication.domain.model.LoginRequest
import com.example.englishapplication.domain.repository.UserRepository
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(private val userApiService: UserApiService): UserRepository {
    override suspend fun login(loginRequest: LoginRequest): Response<Unit> {
        return userApiService.login(loginRequest.username, loginRequest.password)
    }

    override suspend fun createUser(createUserRequest: CreateUserRequest): Response<Unit> {
       return userApiService.createUser(createUserRequest)
    }
}