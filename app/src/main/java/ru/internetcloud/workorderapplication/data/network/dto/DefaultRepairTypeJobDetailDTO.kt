package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class DefaultRepairTypeJobDetailDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("line_number")
    val lineNumber: Int,

    @SerializedName("car_model_id")
    val carModelId: String,

    @SerializedName("car_job_id")
    val carJobId: String,

    @SerializedName("quantity")
    val quantity: BigDecimal,

    @SerializedName("repair_type_id")
    val repairTypeId: String
)
