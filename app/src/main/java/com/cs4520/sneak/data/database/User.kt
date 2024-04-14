package com.cs4520.sneak.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val username: String,
    val email: String,
    var password: String,
)

