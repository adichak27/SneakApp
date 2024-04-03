package com.cs4520.sneak.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cs4520.sneak.model.Shoe
import com.cs4520.sneak.model.User

@Entity(tableName = "shoes_table")
data class Shoes(
    @PrimaryKey val name: String,
    val manufacturer: String,
    val type: String,
    val price : Double,
)

fun Shoes.toShoe(): Shoe {
    return Shoe(this.name, this.manufacturer, this.type, this.price)
}
