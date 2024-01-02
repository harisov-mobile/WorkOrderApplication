package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName

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

    @SerializedName("car_model_id")
    val carModelId: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("release_year")
    val releaseYear: Int,

    @SerializedName("mileage")
    val mileage: Int,

    @SerializedName("owner_id")
    val ownerId: String
)
