package com.example.englishapplication.data.remote

import com.example.englishapplication.domain.model.LoginResponse
import com.example.englishapplication.domain.model.RefreshTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/v1/auth/refresh")
    suspend fun refresh(@Body refreshTokenRequest: RefreshTokenRequest): Response<LoginResponse>

    @POST("/api/v1/auth/logout")
    suspend fun logout(@Body refreshTokenRequest: RefreshTokenRequest): Response<Unit>
}
