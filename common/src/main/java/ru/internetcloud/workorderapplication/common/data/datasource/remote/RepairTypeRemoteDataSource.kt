package ru.internetcloud.workorderapplication.common.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.mapper.RepairTypeMapper
import ru.internetcloud.workorderapplication.common.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.common.data.network.dto.RepairTypeResponse
import ru.internetcloud.workorderapplication.common.domain.model.catalog.RepairType

class RepairTypeRemoteDataSource @Inject constructor(
    private val repairTypeMapper: RepairTypeMapper
) {

    suspend fun getRepairTypeList(): List<RepairType> {
        val repairTypeResponse = ApiClient.getInstance().client.getRepairTypes()
        return repairTypeMapper.fromListDtoToListEntity(repairTypeResponse.repairTypes)
    }

    suspend fun getRepairTypeResponse(): RepairTypeResponse {
        // ToDo переделать ApiClient.getInstance() на зависимость, которую получаем в конструкторе!!!
        return ApiClient.getInstance().client.getRepairTypes()
    }
}
