package com.example.englishapplication.domain.model

data class CreatePhraseRequest(
    val text: String,
    val score: Int,
    val grammarErrors: List<GrammarErrorsRequest>?,
    val correctedText: String?
)
data class GrammarErrorsRequest(
    val incorrect: String,
    val correction: String,
    val explanation: String
)
