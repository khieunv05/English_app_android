package com.example.englishapplication.data.repository

import com.example.englishapplication.data.local.EncryptedTokenStorage
import com.example.englishapplication.data.remote.AuthApiService
import com.example.englishapplication.data.remote.UserApiService
import com.example.englishapplication.domain.model.CreateUserRequest
import com.example.englishapplication.domain.model.LoginRequest
import com.example.englishapplication.domain.model.RefreshTokenRequest
import com.example.englishapplication.domain.repository.UserRepository
import com.example.englishapplication.util.HttpCodeHandler
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(private val userApiService: UserApiService,
    private val authApiService: AuthApiService,
    private val encryptedTokenStorage: EncryptedTokenStorage): UserRepository {
    override suspend fun login(loginRequest: LoginRequest): Result<Unit> {
        return try{
            val response = userApiService.login(loginRequest.username,loginRequest.password)
            if(response.isSuccessful){

                val body = response.body()
                val token = body?.token?.trim()

                if (token.isNullOrEmpty()) {
                    return Result.failure(Exception("Không nhận được token từ server."))
                }

                encryptedTokenStorage.saveAccessToken(token)

                val refreshToken = body?.refreshToken?.trim()
                if (!refreshToken.isNullOrEmpty()) {
                    encryptedTokenStorage.saveRefreshToken(refreshToken)
                }

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

    override suspend fun logout(): Result<Unit> {
        val refreshToken = runCatching { encryptedTokenStorage.getRefreshToken() }.getOrNull()

        val revokeResult = try {
            if (refreshToken.isNullOrBlank()) {
                Result.success(Unit)
            } else {
                val response = authApiService.logout(RefreshTokenRequest(refreshToken))
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(HttpCodeHandler.mapHttpErrorMessage(response.code())))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        runCatching { encryptedTokenStorage.clearAllTokens() }

        return revokeResult
    }
}