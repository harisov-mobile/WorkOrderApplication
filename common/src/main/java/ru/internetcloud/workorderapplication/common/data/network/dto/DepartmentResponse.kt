package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class DepartmentResponse(
    @SerializedName("departments")
    val departments: List<DepartmentDTO>
)
