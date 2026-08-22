package com.example.englishapplication.data.repository

import com.example.englishapplication.data.remote.WordApiService
import com.example.englishapplication.domain.model.CreateWordRequest
import com.example.englishapplication.domain.model.UpdateWordFavoriteRequest
import com.example.englishapplication.domain.model.UpdateWordRequest
import com.example.englishapplication.domain.model.WordData
import com.example.englishapplication.domain.model.WordResponseWithDate
import com.example.englishapplication.domain.repository.WordRepository
import com.example.englishapplication.util.HttpCodeHandler
import javax.inject.Inject

class WordRepositoryImp @Inject constructor(private val wordApiService: WordApiService) : WordRepository {
    override suspend fun getAllWords(): Result<List<WordResponseWithDate>> {
        return try {
            val response = wordApiService.fetchAllWords()
            if(response.isSuccessful){
                Result.success(response.body() ?: emptyList())
            }
            else{
                val errorMsg = HttpCodeHandler.mapHttpErrorMessage(response.code())
                Result.failure(Exception(errorMsg))
            }
        }
        catch (e: Exception){
            Result.failure(e)
        }

    }

    override suspend fun createWord(createWordRequest: CreateWordRequest): Result<WordData> {
        return try {
            val response = wordApiService.createWord(createWordRequest)
            if(response.isSuccessful){
                Result.success(response.body() ?: throw Exception("Response body is null"))
            }
            else{
                val errorMsg = HttpCodeHandler.mapHttpErrorMessage(response.code())
                Result.failure(Exception(errorMsg))
            }
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun updateWord(wordId:Long,updateWordRequest: UpdateWordRequest): WordData {
        return wordApiService.updateWord(wordId, updateWordRequest)
    }

    override suspend fun deleteWord(wordId: Long) {
        wordApiService.deleteWord(wordId)
    }

    override suspend fun updateWordFavorite(wordId: Long,updateWordFavoriteRequest: UpdateWordFavoriteRequest): WordData {
        return wordApiService.updateWordFavorite(wordId,updateWordFavoriteRequest)
    }

    override suspend fun updateReviewCountWord(wordId: Long): WordData {
        return wordApiService.updateWordReviewCount(wordId)
    }

}