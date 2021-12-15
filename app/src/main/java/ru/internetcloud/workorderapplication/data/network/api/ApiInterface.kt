package ru.internetcloud.workorderapplication.data.network.api

import retrofit2.http.GET
import ru.internetcloud.workorderapplication.data.network.dto.AuthResponse
import ru.internetcloud.workorderapplication.data.network.dto.RepairTypeResponse

interface ApiInterface {

    @GET("catalog/repairtype/all")
    suspend fun getRepairTypes(): RepairTypeResponse

    @GET("alpha2/hs/rest/auth/check")
    suspend fun checkAuthorization(): AuthResponse

//    @POST("auth/check")
//    suspend fun checkAuthorization(): AuthResponse
}
