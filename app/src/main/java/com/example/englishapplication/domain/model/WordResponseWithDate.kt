package com.example.englishapplication.domain.model

import java.time.LocalDate

data class WordResponseWithDate(val date: LocalDate,
                                val words : List<WordData>)
