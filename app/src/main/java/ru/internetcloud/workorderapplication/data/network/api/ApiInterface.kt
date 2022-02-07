package ru.internetcloud.workorderapplication.data.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.internetcloud.workorderapplication.data.entity.WorkOrderWithDetails
import ru.internetcloud.workorderapplication.data.network.dto.*

interface ApiInterface {

    companion object {
        private const val PUB_NAME = "alpha2"
        // private const val PUB_NAME = "test"
    }

    @GET(PUB_NAME + "/hs/rest/auth/check")
    suspend fun checkAuthorization(): AuthResponse

    @GET(PUB_NAME + "/hs/rest/catalog/repairtype/all")
    suspend fun getRepairTypes(): RepairTypeResponse

    @GET(PUB_NAME + "/hs/rest/catalog/carjob/all")
    suspend fun getCarJobs(): CarJobResponse

    @GET(PUB_NAME + "/hs/rest/catalog/department/all")
    suspend fun getDepartments(): DepartmentResponse

    @GET(PUB_NAME + "/hs/rest/catalog/employee/all")
    suspend fun getEmployees(): EmployeeResponse

    @GET(PUB_NAME + "/hs/rest/catalog/partner/all")
    suspend fun getPartners(): PartnerResponse

    @GET(PUB_NAME + "/hs/rest/catalog/car/all")
    suspend fun getCars(): CarResponse

    @GET(PUB_NAME + "/hs/rest/catalog/workinghour/all")
    suspend fun getWorkingHours(): WorkingHourResponse

    @GET(PUB_NAME + "/hs/rest/document/workorder/all")
    suspend fun getWorkOrders(): WorkOrderResponse

    @GET(PUB_NAME + "/hs/rest/work_order_settings")
    suspend fun getDefaultWorkOrderSettings(): DefaultWorkOrderSettingsResponse

    @POST(PUB_NAME + "/hs/rest/document/workorder/upload")
    suspend fun uploadWorkOrders(@Body listWO: List<WorkOrderWithDetails>): UploadWorkOrderResponse
}
