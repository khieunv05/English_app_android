package com.example.englishapplication.domain.model

import kotlinx.serialization.SerialName
import java.time.LocalDateTime

data class WordData(
    @SerialName("createdAt")
    val createdAt: LocalDateTime,
    @SerialName("english")
    val english: String,
    @SerialName("example")
    val example: String,
    @SerialName("exampleTranslation")
    val exampleTranslation: String,
    @SerialName("favorite")
    val favorite: Boolean,
    @SerialName("id")
    val id: Long,
    @SerialName("level")
    val level: String,
    @SerialName("nextReview")
    val nextReview: LocalDateTime?,
    @SerialName("partOfSpeech")
    val partOfSpeech: String,
    @SerialName("pronunciation")
    val pronunciation: String,
    @SerialName("reviewCount")
    val reviewCount: Int,
    @SerialName("vietnamese")
    val vietnamese: String
)