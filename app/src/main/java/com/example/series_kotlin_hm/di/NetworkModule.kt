package com.example.series_kotlin_hm.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("accept", "application/json")
                    .addHeader("X-API-KEY", "MGK1QMN-G6MM63G-QHWTBQE-TC95R2J")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    single {
        Json {
            explicitNulls = false
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.kinopoisk.dev/")
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .client(get())
            .build()
    }
}