package com.example.englishapplication.domain.model

data class GeminiWordResponse(
    val english: String,
    val pronunciation : String,
    val vietnamese: String,
    val example: String,
    val exampleTranslation: String,
)
