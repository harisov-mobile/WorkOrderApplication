package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class CarDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("code_1c")
    val code1C: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("vin")
    val vin: String,

    @SerializedName("manufacturer")
    val manufacturer: String,

    @SerializedName("model")
    val model: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("release_year")
    val releaseYear: Int,

    @SerializedName("mileage")
    val mileage: Int,

    @SerializedName("owner_id")
    val ownerId: String
)
