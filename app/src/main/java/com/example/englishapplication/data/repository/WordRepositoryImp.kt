package com.example.englishapplication.data.repository

import com.example.englishapplication.data.remote.WordApiService
import com.example.englishapplication.domain.model.CreateWordRequest
import com.example.englishapplication.domain.model.ListWordUpdateRequest
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

    override suspend fun updateWord(wordId:Long,updateWordRequest: UpdateWordRequest): Result<WordData> {
        return try{
            val result = wordApiService.updateWord(wordId,updateWordRequest)
            if(result.isSuccessful){
                val body = result.body()
                if(body!= null){
                    Result.success(body)
                }
                else{
                    Result.failure(Exception("Server trả về dữ liệu rỗng, vui lòng thử lại"))
                }
            }
            else{
                val msg = HttpCodeHandler.mapHttpErrorMessage(result.code())
                Result.failure(Exception(msg))
            }

        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteWord(wordId: Long): Result<Unit> {
        return try {
            val response = wordApiService.deleteWord(wordId)
            if(response.isSuccessful){
                Result.success(Unit)
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

    override suspend fun updateWordFavorite(wordId: Long,updateWordFavoriteRequest: UpdateWordFavoriteRequest): WordData {
        return wordApiService.updateWordFavorite(wordId,updateWordFavoriteRequest)
    }

    override suspend fun updateReviewCountWord(wordId: Long): WordData {
        return wordApiService.updateWordReviewCount(wordId)
    }

    override suspend fun getWordById(wordId: Long): Result<WordData> {
        return try {
            val result = wordApiService.getWordById(wordId)
            if(result.isSuccessful){
                val body = result.body()
                if(body != null){
                    Result.success(body)
                }
                else{
                    Result.failure(Exception("Server trả về dữ liệu rỗng, vui lòng thử lại"))
                }
            }
            else{
                val msg = HttpCodeHandler.mapHttpErrorMessage(result.code())
                Result.failure(Exception(msg))
            }
        }
        catch (e: Exception){
            Result.failure(Exception(e))
        }
    }

    override suspend fun updateListWordUpdate(wordIds: ListWordUpdateRequest): Result<List<WordData>> {
        return try {
            val result = wordApiService.updateListWordUpdate(wordIds)
            if(result.isSuccessful){
                val body = result.body()
                if(body != null){
                    Result.success(body)
                }
                else{
                    Result.failure(Exception("Server trả về dữ liệu rỗng, vui lòng thử lại"))
                }
            }
            else{
                val msg = HttpCodeHandler.mapHttpErrorMessage(result.code())
                Result.failure(Exception(msg))
            }
        }
        catch (e: Exception){
            Result.failure(Exception(e))
        }
    }

}