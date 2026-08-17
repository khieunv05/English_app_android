package com.example.englishapplication.domain.model

data class UpdateWordRequest(
    val english: String,
    val vietnamese: String,
    val example: String,
    val exampleTranslation: String,
    val pronunciation: String,
    val partOfSpeech: String,
    val level: String
)
