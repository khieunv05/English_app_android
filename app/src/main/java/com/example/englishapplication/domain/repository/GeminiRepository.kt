package com.example.englishapplication.domain.repository

import com.example.englishapplication.domain.model.GeminiWordRequest
import com.example.englishapplication.domain.model.GeminiWordResponse

interface GeminiRepository {
    suspend fun generateWordInfo(geminiWordRequest: GeminiWordRequest): Result<GeminiWordResponse>
}