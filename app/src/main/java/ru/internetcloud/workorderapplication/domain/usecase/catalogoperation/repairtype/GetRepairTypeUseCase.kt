package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype

import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

class GetRepairTypeUseCase(private val repairTypeRepository: RepairTypeRepository) {
    suspend fun getRepairType(id: String): RepairType? {
        return repairTypeRepository.getRepairType(id)
    }
}
