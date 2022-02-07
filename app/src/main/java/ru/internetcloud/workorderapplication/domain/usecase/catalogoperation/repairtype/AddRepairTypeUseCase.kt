package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype

import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

class AddRepairTypeUseCase(private val repairTypeRepository: RepairTypeRepository) {
    suspend fun addRepairType(repairType: RepairType) {
        return repairTypeRepository.addRepairType(repairType)
    }
}
