package com.example.englishapplication.di

import com.example.englishapplication.data.local.EncryptedTokenStorage
import com.example.englishapplication.util.AuthEventManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authEventManager: AuthEventManager,
    private val encryptedTokenStorage: EncryptedTokenStorage
) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            encryptedTokenStorage.getAccessToken()
        }

        val requestBuilder = chain.request().newBuilder()
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()
        val response = chain.proceed(request)
        if (response.code == 401) {
            runBlocking {
                encryptedTokenStorage.clearAccessToken()
                authEventManager.notifyUnauthorized()
            }
        }

        return response
    }

}