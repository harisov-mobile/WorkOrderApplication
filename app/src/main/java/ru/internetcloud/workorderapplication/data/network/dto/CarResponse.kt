package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class CarResponse(
    @SerializedName("cars")
    val cars: List<CarDTO>
)
