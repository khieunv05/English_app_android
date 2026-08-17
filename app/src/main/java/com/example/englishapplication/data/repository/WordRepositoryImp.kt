package com.example.englishapplication.data.repository

import com.example.englishapplication.data.remote.WordApiService
import com.example.englishapplication.domain.model.CreateWordRequest
import com.example.englishapplication.domain.model.UpdateWordFavoriteRequest
import com.example.englishapplication.domain.model.UpdateWordRequest
import com.example.englishapplication.domain.model.WordResponse
import com.example.englishapplication.domain.repository.WordRepository
import javax.inject.Inject

class WordRepositoryImp @Inject constructor(private val wordApiService: WordApiService) : WordRepository {
    override suspend fun getAllWords(): List<WordResponse> {
        return wordApiService.fetchAllWords()
    }

    override suspend fun createWord(createWordRequest: CreateWordRequest): WordResponse {
        return wordApiService.createWord(createWordRequest)
    }

    override suspend fun updateWord(wordId:Long,updateWordRequest: UpdateWordRequest): WordResponse {
        return wordApiService.updateWord(wordId, updateWordRequest)
    }

    override suspend fun deleteWord(wordId: Long) {
        wordApiService.deleteWord(wordId)
    }

    override suspend fun updateWordFavorite(wordId: Long,updateWordFavoriteRequest: UpdateWordFavoriteRequest): WordResponse {
        return wordApiService.updateWordFavorite(wordId,updateWordFavoriteRequest)
    }

    override suspend fun updateReviewCountWord(wordId: Long): WordResponse {
        return wordApiService.updateWordReviewCount(wordId)
    }
}