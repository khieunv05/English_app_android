package com.example.englishapplication.domain.repository

import com.example.englishapplication.domain.model.GeminiPhraseRequest
import com.example.englishapplication.domain.model.GeminiPhraseResponse
import com.example.englishapplication.domain.model.GeminiWordRequest
import com.example.englishapplication.domain.model.GeminiWordResponse
import retrofit2.http.Body

interface GeminiRepository {
    suspend fun generateWordInfo(geminiWordRequest: GeminiWordRequest): Result<GeminiWordResponse>
    suspend fun scoreParagraph(@Body geminiPhraseRequest: GeminiPhraseRequest): Result<GeminiPhraseResponse>
}