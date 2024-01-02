package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class CarResponse(
    @SerializedName("cars")
    val cars: List<CarDTO>
)
