package com.cs4520.sneak.data

import com.cs4520.sneak.model.Shoe
import com.cs4520.sneak.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET(SneakApi.USERS)
    suspend fun getUsers(@Query("username") username: String?): Response<List<User>>

    @GET(SneakApi.SHOES)
    suspend fun getShoes(): Response<List<Shoe>>

}