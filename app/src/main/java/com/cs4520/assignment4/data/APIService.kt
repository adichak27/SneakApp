package com.cs4520.assignment4.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

@GET(UsersApi.USERS)
    suspend fun getUser(@Query("username")username: String?): Response<Int>

}