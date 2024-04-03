package com.cs4520.sneak.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cs4520.sneak.model.User
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class Users(
    @PrimaryKey val username: String,
    val email: String,
    val password: String,
)

fun Users.toUser(): User {
    return User(email = this.email, username = this.username, password = this.password)
}

