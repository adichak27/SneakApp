package com.cs4520.assignment4.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UsersApi {
    private const val BASE_URL: String = "https://bhampstead.pythonanywhere.com/sneak/"
    const val USERS: String = "users/"

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