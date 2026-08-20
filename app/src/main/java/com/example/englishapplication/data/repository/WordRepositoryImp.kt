package com.example.englishapplication.data.repository

import com.example.englishapplication.data.remote.WordApiService
import com.example.englishapplication.domain.model.CreateWordRequest
import com.example.englishapplication.domain.model.UpdateWordFavoriteRequest
import com.example.englishapplication.domain.model.UpdateWordRequest
import com.example.englishapplication.domain.model.WordResponse
import com.example.englishapplication.domain.repository.WordRepository
import retrofit2.Response
import javax.inject.Inject

class WordRepositoryImp @Inject constructor(private val wordApiService: WordApiService) : WordRepository {
    override suspend fun getAllWords(): Result<List<WordResponse>> {
        return try {
            val response = wordApiService.fetchAllWords()
            if(response.isSuccessful){
                Result.success(response.body() ?: emptyList())
            }
            else{
                Result.failure(Exception(response.errorBody()?.string() ?: "Lấy từ thất bại"))
            }
        }
        catch (e: Exception){
            Result.failure(e)
        }

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