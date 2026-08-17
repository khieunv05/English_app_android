package com.example.englishapplication.domain.model

import kotlinx.serialization.SerialName
import java.time.LocalDateTime

data class PhraseResponse(
    @SerialName("correctedText")
    val correctedText: String?,
    @SerialName("createdAt")
    val createdAt: LocalDateTime,
    @SerialName("grammarErrors")
    val grammarErrors: List<GrammarErrors>?,
    @SerialName("id")
    val id : Long,
    @SerialName("score")
    val score: Int,
    @SerialName("text")
    val text: String,
)
data class GrammarErrors(
    @SerialName("correction")
    val correction: String,
    @SerialName("explanation")
    val explanation: String,
    @SerialName("id")
    val id: Long,
    @SerialName("incorrect")
    val incorrect: String
)
