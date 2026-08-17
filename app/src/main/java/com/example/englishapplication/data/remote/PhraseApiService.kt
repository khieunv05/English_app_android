package com.example.englishapplication.data.remote

import com.example.englishapplication.domain.model.CreatePhraseRequest
import com.example.englishapplication.domain.model.PhraseResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PhraseApiService {
    @GET("/api/v1/phrases")
    suspend fun getAllPhrases(): List<PhraseResponse>
    @POST("/api/v1/phrases")
    suspend fun createPhrase(@Body createPhraseRequest: CreatePhraseRequest): PhraseResponse
    @DELETE("/api/v1/phrases/{phraseId}")
    suspend fun deletePhrase(@Path("phraseId") phraseId: Long)
}