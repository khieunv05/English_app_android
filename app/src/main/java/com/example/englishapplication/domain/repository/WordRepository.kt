package com.example.englishapplication.domain.repository

import com.example.englishapplication.domain.model.CreateWordRequest
import com.example.englishapplication.domain.model.UpdateWordFavoriteRequest
import com.example.englishapplication.domain.model.UpdateWordRequest
import com.example.englishapplication.domain.model.WordResponse
import retrofit2.Response

interface WordRepository {
    suspend fun getAllWords(): Result<List<WordResponse>>
    suspend fun createWord(createWordRequest: CreateWordRequest): WordResponse
    suspend fun updateWord(wordId:Long,updateWordRequest: UpdateWordRequest): WordResponse
    suspend fun deleteWord(wordId: Long)
    suspend fun updateWordFavorite(wordId: Long,updateWordFavoriteRequest: UpdateWordFavoriteRequest): WordResponse
    suspend fun updateReviewCountWord(wordId: Long): WordResponse

}