package com.cs4520.sneak.model

import com.cs4520.sneak.data.database.Users
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,

)

fun User.toUsers(): Users {
    return Users(username = username, email = email, password = password)
}
