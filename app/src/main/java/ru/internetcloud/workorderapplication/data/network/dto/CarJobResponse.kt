package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class CarJobResponse(
    @SerializedName("carjobs")
    val carJobs: List<CarJobDTO>
)
