package com.example.englishapplication.di

import com.example.englishapplication.data.local.EncryptedTokenStorage
import com.example.englishapplication.data.remote.PhraseApiService
import com.example.englishapplication.data.remote.UserApiService
import com.example.englishapplication.data.remote.WordApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(encryptedTokenStorage: EncryptedTokenStorage): OkHttpClient{
        return OkHttpClient.Builder().addInterceptor {
            chain->
            val originalRequest = chain.request()
            val myToken = runBlocking {
                encryptedTokenStorage.getAccessToken()
            }
            val requestBuilder = originalRequest.newBuilder()
                .header("Accept","application/json")

            if (!myToken.isNullOrEmpty()) {
                requestBuilder.header("Authorization","Bearer $myToken")
            }
            
            chain.proceed(requestBuilder.build())
        }
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.5:8080")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
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

}