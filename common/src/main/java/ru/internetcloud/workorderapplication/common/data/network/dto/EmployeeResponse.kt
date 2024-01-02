package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class EmployeeResponse(
    @SerializedName("employees")
    val employees: List<EmployeeDTO>
)
