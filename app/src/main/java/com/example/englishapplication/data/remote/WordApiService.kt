package com.example.englishapplication.data.remote

import com.example.englishapplication.domain.model.CreateWordRequest
import com.example.englishapplication.domain.model.UpdateWordFavoriteRequest
import com.example.englishapplication.domain.model.UpdateWordRequest
import com.example.englishapplication.domain.model.WordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WordApiService {
    @GET("/api/v1/words/me")
    suspend fun fetchAllWords(): Response<List<WordResponse>>
    @POST("/api/v1/words")
    suspend fun createWord(@Body createWordRequest: CreateWordRequest): WordResponse
    @PUT("/api/v1/words/{wordId}")
    suspend fun updateWord(@Path("wordId") wordId: Long, @Body updateWordRequest: UpdateWordRequest): WordResponse
    @DELETE("/api/v1/words/{wordId}")
    suspend fun deleteWord(@Path("wordId") wordId: Long)

    @PUT("/api/v1/words/{wordId}/favorite")
    suspend fun updateWordFavorite(@Path("wordId") wordId: Long,
                                   @Body updateWordFavoriteRequest: UpdateWordFavoriteRequest): WordResponse
    @PUT("/api/v1/words/{wordId}/review")
    suspend fun updateWordReviewCount(@Path("wordId") wordId: Long): WordResponse
}