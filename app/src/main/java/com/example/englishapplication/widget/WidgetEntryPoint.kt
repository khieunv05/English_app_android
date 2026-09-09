package com.example.englishapplication.widget

import android.content.Context
import com.example.englishapplication.domain.model.WordData
import com.example.englishapplication.domain.repository.WordRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun wordRepository(): WordRepository
}

suspend fun fetchWordsNeedReview(context: Context): List<WordData> {
    val entryPoint = EntryPointAccessors.fromApplication(
        context.applicationContext,
        WidgetEntryPoint::class.java
    )
    val wordRepository = entryPoint.wordRepository()

    val result = wordRepository.getAllWords()
    return result.fold(
        onSuccess = { words ->
            val today = java.time.LocalDate.now()
            words.flatMap { it.words }
                .filter { it.nextReview == null || it.nextReview.toLocalDate() <= today }
        },
        onFailure = { emptyList() }
    )
}