package com.example.englishapplication.domain.repository

import com.example.englishapplication.domain.model.CreatePhraseRequest
import com.example.englishapplication.domain.model.PhraseResponse

interface PhraseRepository {
    suspend fun getAllPhrases(): List<PhraseResponse>
    suspend fun createPhrase(addPhraseRequest: CreatePhraseRequest): PhraseResponse
    suspend fun deletePhrase(phraseId: Long)
}