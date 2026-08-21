package com.example.englishapplication.domain.repository

import com.example.englishapplication.domain.model.CreateWordRequest
import com.example.englishapplication.domain.model.UpdateWordFavoriteRequest
import com.example.englishapplication.domain.model.UpdateWordRequest
import com.example.englishapplication.domain.model.WordData
import com.example.englishapplication.domain.model.WordResponseWithDate

interface WordRepository {
    suspend fun getAllWords(): Result<List<WordResponseWithDate>>
    suspend fun createWord(createWordRequest: CreateWordRequest): WordData
    suspend fun updateWord(wordId:Long,updateWordRequest: UpdateWordRequest): WordData
    suspend fun deleteWord(wordId: Long)
    suspend fun updateWordFavorite(wordId: Long,updateWordFavoriteRequest: UpdateWordFavoriteRequest): WordData
    suspend fun updateReviewCountWord(wordId: Long): WordData

}