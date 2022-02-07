package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class EmployeeResponse(
    @SerializedName("employees")
    val employees: List<EmployeeDTO>
)
