package com.example.englishapplication.di

import com.example.englishapplication.data.local.EncryptedTokenStorage
import com.example.englishapplication.data.remote.GeminiApiService
import com.example.englishapplication.data.remote.LocalDateTimeAdapter
import com.example.englishapplication.data.remote.PhraseApiService
import com.example.englishapplication.data.remote.UserApiService
import com.example.englishapplication.data.remote.WordApiService
import com.example.englishapplication.type_adapter.LocalDateAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit{
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.5:8080")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    @Provides
    @Singleton
    fun providePhraseApiService(retrofit: Retrofit): PhraseApiService{
        return retrofit.create(PhraseApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideWordApiService(retrofit: Retrofit): WordApiService{
        return retrofit.create(WordApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService{
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGeminiApiService(retrofit: Retrofit): GeminiApiService{
        return retrofit.create(GeminiApiService::class.java)
    }

}