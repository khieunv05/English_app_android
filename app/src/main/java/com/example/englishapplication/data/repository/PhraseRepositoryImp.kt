package com.example.englishapplication.data.repository

import com.example.englishapplication.data.remote.PhraseApiService
import com.example.englishapplication.domain.model.CreatePhraseRequest
import com.example.englishapplication.domain.model.PhraseResponse
import com.example.englishapplication.domain.repository.PhraseRepository
import com.example.englishapplication.util.HttpCodeHandler
import retrofit2.Response
import javax.inject.Inject

class PhraseRepositoryImp @Inject constructor(private val phraseApiService: PhraseApiService) : PhraseRepository {
    override suspend fun getAllPhrases(): Result<List<PhraseResponse>> {
        return try {
           val result= phraseApiService.getAllPhrases()
            if(result.isSuccessful){
                val body = result.body()
                if(body != null){
                    Result.success(body)
                }
                else Result.failure(Exception("Server trả về dữ liệu rỗng, vui lòng thử lại"))
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

    override suspend fun createPhrase(addPhraseRequest: CreatePhraseRequest): PhraseResponse {
       return phraseApiService.createPhrase(addPhraseRequest)
    }

    override suspend fun deletePhrase(phraseId: Long) {
        return phraseApiService.deletePhrase(phraseId)
    }
}