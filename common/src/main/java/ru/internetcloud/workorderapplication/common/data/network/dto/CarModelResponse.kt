package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class CarModelResponse(
    @SerializedName("carmodels")
    val carModels: List<CarModelDTO>
)
