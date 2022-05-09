package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class CarModelResponse(
    @SerializedName("carmodels")
    val carModels: List<CarModelDTO>
)
