package com.example.englishapplication.data.remote

import com.example.englishapplication.domain.model.CreateUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApiService {
    @POST("/api/v1/users")
    suspend fun createUser(@Body createUserRequest: CreateUserRequest): Response<Unit>

    @FormUrlEncoded
    @POST("/api/v1/auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Unit>
}
