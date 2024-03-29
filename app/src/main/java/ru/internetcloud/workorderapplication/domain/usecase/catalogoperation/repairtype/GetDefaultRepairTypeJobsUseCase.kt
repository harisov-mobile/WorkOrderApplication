package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype

import ru.internetcloud.workorderapplication.domain.model.catalog.DefaultRepairTypeJobDetail
import ru.internetcloud.workorderapplication.domain.model.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository
import javax.inject.Inject

class GetDefaultRepairTypeJobsUseCase @Inject constructor(
    private val repairTypeRepository: RepairTypeRepository
) {
    suspend fun getDefaultRepairTypeJobDetails(repairType: RepairType): List<DefaultRepairTypeJobDetail> {
        return repairTypeRepository.getDefaultRepairTypeJobDetails(repairType)
    }
}
