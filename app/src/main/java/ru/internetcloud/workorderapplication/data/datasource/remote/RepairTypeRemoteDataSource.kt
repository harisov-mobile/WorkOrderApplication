package ru.internetcloud.workorderapplication.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.mapper.RepairTypeMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.RepairTypeResponse
import ru.internetcloud.workorderapplication.domain.catalog.RepairType

class RepairTypeRemoteDataSource @Inject constructor(
    private val repairTypeMapper: RepairTypeMapper
) {

    suspend fun getRepairTypeList(): List<RepairType> {
        val repairTypeResponse = ApiClient.getInstance().client.getRepairTypes()
        return repairTypeMapper.fromListDtoToListEntity(repairTypeResponse.repairTypes)
    }

    suspend fun getRepairTypeResponse(): RepairTypeResponse {
        return ApiClient.getInstance().client.getRepairTypes()
    }
}
