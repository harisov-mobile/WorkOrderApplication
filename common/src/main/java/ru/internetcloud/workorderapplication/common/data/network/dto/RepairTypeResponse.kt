package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class RepairTypeResponse(
    @SerializedName("repairtypes")
    val repairTypes: List<RepairTypeDTO>,

    @SerializedName("default_job_details")
    val defaultJobDetails: List<DefaultRepairTypeJobDetailDTO>
)
