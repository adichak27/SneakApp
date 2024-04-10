package com.cs4520.sneak.data

import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.data.database.User

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET(SneakApi.USERS)
    suspend fun getUsers(): Response<List<User>>

    @GET(SneakApi.USERS + "/{userName}")
    suspend fun getUser(@Path(value="userName")userName: String): Response<User>

    @POST(SneakApi.USERS + "/{userName}")
    suspend fun editUser(@Path(value="userName")userName: String,
                         @Body body: Map<String, String>): Response<Any>

    @GET(SneakApi.SHOES)
    suspend fun getShoes(): Response<List<Shoe>>

}