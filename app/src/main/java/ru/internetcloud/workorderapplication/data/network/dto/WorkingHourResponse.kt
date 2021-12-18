package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class WorkingHourResponse(
    @SerializedName("workinghours")
    val workingHours: List<WorkingHourDTO>
)