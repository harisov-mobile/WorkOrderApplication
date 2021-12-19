package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class WorkingHourDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("code_1c")
    val code1C: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: BigDecimal
)
