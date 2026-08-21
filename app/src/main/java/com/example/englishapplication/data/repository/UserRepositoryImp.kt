package com.example.englishapplication.data.repository

import com.example.englishapplication.data.local.EncryptedTokenStorage
import com.example.englishapplication.data.remote.UserApiService
import com.example.englishapplication.domain.model.CreateUserRequest
import com.example.englishapplication.domain.model.LoginRequest
import com.example.englishapplication.domain.repository.UserRepository
import com.example.englishapplication.util.HttpCodeHandler
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(private val userApiService: UserApiService,
    private val encryptedTokenStorage: EncryptedTokenStorage): UserRepository {
    override suspend fun login(loginRequest: LoginRequest): Result<Unit> {
        return try{
            val response = userApiService.login(loginRequest.username,loginRequest.password)
            if(response.isSuccessful){

                val authHeader = response.headers()["Authorization"]
                val token = when {
                    authHeader == null -> null
                    authHeader.startsWith("Bearer ", ignoreCase = true) -> authHeader.substring(7).trim()
                    else -> authHeader.trim()
                }

                if (token.isNullOrEmpty()) {
                    return Result.failure(Exception("Không nhận được token từ server."))
                }

                encryptedTokenStorage.saveAccessToken(token)
                Result.success(Unit)

            }
            else{
                val message = HttpCodeHandler.mapHttpErrorMessage(response.code())
                Result.failure(Exception(message))
            }
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun createUser(createUserRequest: CreateUserRequest): Result<Unit> {
        return try{
            val response = userApiService.createUser(createUserRequest)
            if(response.isSuccessful){
                Result.success(Unit)
            }
            else{
                val message = HttpCodeHandler.mapHttpErrorMessage(response.code())
                Result.failure(Exception(message))
            }
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }
}