package com.example.englishapplication.data.repository

import com.example.englishapplication.data.remote.GeminiApiService
import com.example.englishapplication.domain.model.GeminiWordRequest
import com.example.englishapplication.domain.model.GeminiWordResponse
import com.example.englishapplication.domain.repository.GeminiRepository
import javax.inject.Inject

class GeminiRepositoryImp @Inject constructor(
    private val geminiApiService: GeminiApiService
) : GeminiRepository {
    override suspend fun generateWordInfo(geminiWordRequest: GeminiWordRequest): GeminiWordResponse {
        return geminiApiService.generateWordInfo(geminiWordRequest)
    }
}