package com.example.englishapplication.domain.repository

import com.example.englishapplication.domain.model.CreatePhraseRequest
import com.example.englishapplication.domain.model.PhraseResponse

interface PhraseRepository {
    suspend fun getAllPhrases(): Result<List<PhraseResponse>>
    suspend fun createPhrase(addPhraseRequest: CreatePhraseRequest): Result<PhraseResponse>
    suspend fun deletePhrase(phraseId: Long) : Result<Unit>
    suspend fun findPhraseById(phraseId: Long): Result<PhraseResponse>
}