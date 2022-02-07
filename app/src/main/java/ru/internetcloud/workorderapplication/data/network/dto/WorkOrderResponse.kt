package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class WorkOrderResponse(
    @SerializedName("workorders")
    val workOrders: List<WorkOrderDTO>,

    @SerializedName("jobdetails")
    val jobDetails: List<JobDetailDTO>,

    @SerializedName("performers")
    val performerDetails: List<PerformerDetailDTO>
)
