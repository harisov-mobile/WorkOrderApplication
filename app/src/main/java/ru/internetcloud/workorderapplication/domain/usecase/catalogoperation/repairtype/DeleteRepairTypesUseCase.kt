package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype

import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

class DeleteRepairTypesUseCase(private val repairTypeRepository: RepairTypeRepository) {
    suspend fun deleteAllRepairTypes() {
        return repairTypeRepository.deleteAllRepairTypes()
    }
}
