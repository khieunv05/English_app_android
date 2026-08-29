package com.example.englishapplication.domain.model

data class GeminiPhraseResponse(
    val text: String,
    val score: Int,
    val grammarErrors: List<GeminiGrammarError>,
    val correctedText: String
)
data class GeminiGrammarError(
    val incorrect: String,
    val correction: String,
    val explanation: String
)
