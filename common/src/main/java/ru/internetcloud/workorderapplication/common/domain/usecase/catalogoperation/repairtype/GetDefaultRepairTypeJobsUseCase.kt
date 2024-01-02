package ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.repairtype

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.catalog.DefaultRepairTypeJobDetail
import ru.internetcloud.workorderapplication.common.domain.model.catalog.RepairType
import ru.internetcloud.workorderapplication.common.domain.repository.RepairTypeRepository

class GetDefaultRepairTypeJobsUseCase @Inject constructor(
    private val repairTypeRepository: RepairTypeRepository
) {
    suspend fun getDefaultRepairTypeJobDetails(repairType: RepairType): List<DefaultRepairTypeJobDetail> {
        return repairTypeRepository.getDefaultRepairTypeJobDetails(repairType)
    }
}
