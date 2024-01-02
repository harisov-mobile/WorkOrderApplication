package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class PartnerResponse(
    @SerializedName("partners")
    val partners: List<PartnerDTO>
)
