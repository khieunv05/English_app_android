package com.example.englishapplication.data.repository

import com.example.englishapplication.data.local.EncryptedTokenStorage
import com.example.englishapplication.data.remote.AuthApiService
import com.example.englishapplication.domain.model.RefreshTokenRequest
import com.example.englishapplication.util.AuthEventManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefresher @Inject constructor(
    private val authApiService: AuthApiService,
    private val encryptedTokenStorage: EncryptedTokenStorage,
    private val authEventManager: AuthEventManager
) {
    private val mutex = Mutex()

    fun refresh(rejectedToken: String?): String? = runBlocking {
        mutex.withLock {
            val currentToken = encryptedTokenStorage.getAccessToken()
            if (!currentToken.isNullOrBlank() && currentToken != rejectedToken) {
                return@withLock currentToken
            }

            val refreshToken = encryptedTokenStorage.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                clearSession()
                return@withLock null
            }

            val response = try {
                authApiService.refresh(RefreshTokenRequest(refreshToken))
            } catch (_: Exception) {
                return@withLock null
            }

            val body = response.body()
            val newToken = body?.token?.trim()
            if (!response.isSuccessful || newToken.isNullOrEmpty()) {
                clearSession()
                return@withLock null
            }

            encryptedTokenStorage.saveAccessToken(newToken)
            body.refreshToken?.trim()?.takeIf { it.isNotEmpty() }
                ?.let { encryptedTokenStorage.saveRefreshToken(it) }

            newToken
        }
    }

    private suspend fun clearSession() {
        encryptedTokenStorage.clearAllTokens()
        authEventManager.notifyUnauthorized()
    }
}