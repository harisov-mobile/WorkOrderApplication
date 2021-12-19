package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class JobDetailDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("car_job_id")
    val carJobId: String,

    @SerializedName("quantity")
    val quantity: BigDecimal,

    @SerializedName("time_norm")
    val timeNorm: BigDecimal,

    @SerializedName("working_hour_id")
    val workingHourId: String,

    @SerializedName("sum")
    val sum: BigDecimal
)
