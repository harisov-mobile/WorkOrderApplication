package ru.internetcloud.workorderapplication.data.network.api

import retrofit2.http.GET
import ru.internetcloud.workorderapplication.data.network.dto.RepairTypeResponse

interface ApiInterface {

    @GET("catalog/repairtype/all")
    fun getRepairTypes(): RepairTypeResponse
}