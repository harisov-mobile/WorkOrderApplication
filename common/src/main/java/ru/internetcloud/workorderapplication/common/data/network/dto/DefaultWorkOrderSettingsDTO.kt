package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class DefaultWorkOrderSettingsDTO(
    @SerializedName("id_department")
    val departmentId: String,

    @SerializedName("id_employee")
    val employeeId: String,

    @SerializedName("id_master")
    val masterId: String,

    @SerializedName("id_working_hour")
    val workingHourId: String,

    @SerializedName("default_time_norm")
    val defaultTimeNorm: BigDecimal
)
