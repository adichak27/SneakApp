package com.cs4520.sneak.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shoes_table")
data class Shoe(
    @PrimaryKey val name: String,
    val manufacturer: String,
    val type: String,
    val price : Double,
)
