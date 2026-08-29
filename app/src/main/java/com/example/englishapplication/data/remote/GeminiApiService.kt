package com.example.englishapplication.data.remote

import com.example.englishapplication.domain.model.GeminiPhraseRequest
import com.example.englishapplication.domain.model.GeminiPhraseResponse
import com.example.englishapplication.domain.model.GeminiWordRequest
import com.example.englishapplication.domain.model.GeminiWordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GeminiApiService  {
    @POST("/api/v1/gemini/generate")
    suspend fun generateWordInfo(@Body geminiWordRequest: GeminiWordRequest) : Response<GeminiWordResponse>

    @POST("/api/v1/gemini/phrase")
    suspend fun scoreParagraph(@Body geminiPhraseRequest: GeminiPhraseRequest): Response<GeminiPhraseResponse>
}