package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation

import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

class GetRepairTypeUseCase(private val repairTypeRepository: RepairTypeRepository) {
    suspend fun getRepairType(id: Int): RepairType? {
        return repairTypeRepository.getRepairType(id)
    }
}
