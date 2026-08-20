package com.example.englishapplication.data.remote

import com.example.englishapplication.domain.model.GeminiWordRequest
import com.example.englishapplication.domain.model.GeminiWordResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GeminiApiService  {
    @POST("/api/v1/gemini/generate")
    suspend fun generateWordInfo(@Body geminiWordRequest: GeminiWordRequest) : GeminiWordResponse
}