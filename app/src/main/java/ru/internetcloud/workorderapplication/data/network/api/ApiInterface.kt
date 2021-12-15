package ru.internetcloud.workorderapplication.data.network.api

import retrofit2.http.GET
import ru.internetcloud.workorderapplication.data.network.dto.AuthResponse
import ru.internetcloud.workorderapplication.data.network.dto.CarJobResponse
import ru.internetcloud.workorderapplication.data.network.dto.RepairTypeResponse

interface ApiInterface {

    @GET("alpha2/hs/rest/auth/check")
    suspend fun checkAuthorization(): AuthResponse

    @GET("alpha2/hs/rest/catalog/repairtype/all")
    suspend fun getRepairTypes(): RepairTypeResponse

    @GET("alpha2/hs/rest/catalog/carjob/all")
    suspend fun getCarJobs(): CarJobResponse

//    @POST("auth/check")
//    suspend fun checkAuthorization(): AuthResponse
}
