package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class WorkingHourResponse(
    @SerializedName("workinghours")
    val workingHours: List<WorkingHourDTO>
)
