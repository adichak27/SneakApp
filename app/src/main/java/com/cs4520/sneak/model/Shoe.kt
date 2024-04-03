package com.cs4520.sneak.model

import com.google.gson.annotations.SerializedName

data class Shoe(
    @SerializedName("name")
    val name: String,
    @SerializedName("manufacturer")
    val manufacturer: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("price")
    val price : Double,
)
