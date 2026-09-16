package com.example.englishapplication.di

import com.example.englishapplication.data.local.EncryptedTokenStorage
import com.example.englishapplication.data.repository.TokenRefresher
import com.example.englishapplication.util.AuthEventManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authEventManager: AuthEventManager,
    private val encryptedTokenStorage: EncryptedTokenStorage,
    private val tokenRefresher: TokenRefresher
) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking {
            encryptedTokenStorage.getAccessToken()
        }

        val requestBuilder = originalRequest.newBuilder()
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val response = chain.proceed(requestBuilder.build())

        if (response.code != 401 || !isAuthenticatedEndpoint(originalRequest)) {
            return response
        }

        val newToken = tokenRefresher.refresh(token)
        if (newToken.isNullOrBlank()) {
            return response
        }

        response.close()
        val retryResponse = chain.proceed(
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        )

        if (retryResponse.code == 401) {
            runBlocking {
                encryptedTokenStorage.clearAllTokens()
                authEventManager.notifyUnauthorized()
            }
        }

        return retryResponse
    }

    private fun isAuthenticatedEndpoint(request: Request): Boolean {
        val path = request.url.encodedPath
        return AUTH_ENDPOINTS.none { path.endsWith(it) }
    }

    private companion object {
        val AUTH_ENDPOINTS = listOf(
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/v1/auth/logout"
        )
    }
}
