package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype

import ru.internetcloud.workorderapplication.domain.catalog.DefaultRepairTypeJobDetail
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

class GetDefaultRepairTypeJobsUseCase(private val repairTypeRepository: RepairTypeRepository) {

    suspend fun getDefaultRepairTypeJobDetails(repairType: RepairType): List<DefaultRepairTypeJobDetail> {
        return repairTypeRepository.getDefaultRepairTypeJobDetails(repairType)
    }
}
