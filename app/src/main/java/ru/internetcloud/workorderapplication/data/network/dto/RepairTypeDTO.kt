package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class RepairTypeDTO(
    @SerializedName("id1C")
    var id1C: String,

    @SerializedName("name")
    var name: String
)
