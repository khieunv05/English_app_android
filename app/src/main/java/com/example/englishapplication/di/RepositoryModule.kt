package com.example.englishapplication.di

import com.example.englishapplication.data.repository.PhraseRepositoryImp
import com.example.englishapplication.data.repository.UserRepositoryImp
import com.example.englishapplication.data.repository.WordRepositoryImp
import com.example.englishapplication.domain.repository.PhraseRepository
import com.example.englishapplication.domain.repository.UserRepository
import com.example.englishapplication.domain.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWordRepository(wordRepositoryImp: WordRepositoryImp): WordRepository

    @Binds
    @Singleton
    abstract fun bindPhraseRepository(phraseRepositoryImp: PhraseRepositoryImp): PhraseRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImp: UserRepositoryImp): UserRepository
}