package com.example.englishapplication.data.repository

import com.example.englishapplication.data.remote.PhraseApiService
import com.example.englishapplication.domain.model.CreatePhraseRequest
import com.example.englishapplication.domain.model.PhraseResponse
import com.example.englishapplication.domain.repository.PhraseRepository
import javax.inject.Inject

class PhraseRepositoryImp @Inject constructor(private val phraseApiService: PhraseApiService) : PhraseRepository {
    override suspend fun getAllPhrases(): List<PhraseResponse> {
       return phraseApiService.getAllPhrases()
    }

    override suspend fun createPhrase(addPhraseRequest: CreatePhraseRequest): PhraseResponse {
       return phraseApiService.createPhrase(addPhraseRequest)
    }

    override suspend fun deletePhrase(phraseId: Long) {
        return phraseApiService.deletePhrase(phraseId)
    }
}