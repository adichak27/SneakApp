package com.cs4520.sneak.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SneakApi {
    private const val BASE_URL: String = "https://bhampstead.pythonanywhere.com/sneak/"
    const val USERS: String = "users"
    const val SHOES: String = "shoes"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}