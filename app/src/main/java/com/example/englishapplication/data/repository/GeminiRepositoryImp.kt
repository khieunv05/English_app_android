package com.example.englishapplication.data.repository

import com.example.englishapplication.data.remote.GeminiApiService
import com.example.englishapplication.domain.model.GeminiWordRequest
import com.example.englishapplication.domain.model.GeminiWordResponse
import com.example.englishapplication.domain.repository.GeminiRepository
import com.example.englishapplication.util.HttpCodeHandler
import javax.inject.Inject

class GeminiRepositoryImp @Inject constructor(
    private val geminiApiService: GeminiApiService
) : GeminiRepository {
    override suspend fun generateWordInfo(geminiWordRequest: GeminiWordRequest): Result<GeminiWordResponse> {
        return try {
            val result = geminiApiService.generateWordInfo(geminiWordRequest)
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