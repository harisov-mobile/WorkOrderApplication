package ru.internetcloud.workorderapplication.data.network.api

import retrofit2.http.GET
import ru.internetcloud.workorderapplication.data.network.dto.*

interface ApiInterface {

    @GET("alpha2/hs/rest/auth/check")
    suspend fun checkAuthorization(): AuthResponse

    @GET("alpha2/hs/rest/catalog/repairtype/all")
    suspend fun getRepairTypes(): RepairTypeResponse

    @GET("alpha2/hs/rest/catalog/carjob/all")
    suspend fun getCarJobs(): CarJobResponse

    @GET("alpha2/hs/rest/catalog/department/all")
    suspend fun getDepartments(): DepartmentResponse

    @GET("alpha2/hs/rest/catalog/employee/all")
    suspend fun getEmployees(): EmployeeResponse

//    @POST("auth/check")
//    suspend fun checkAuthorization(): AuthResponse
}
