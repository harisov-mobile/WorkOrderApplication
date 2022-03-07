package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype

import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

class GetRepairTypeListUseCase(private val repairTypeRepository: RepairTypeRepository) {

    suspend fun getRepairTypeList(): List<RepairType> {
        return repairTypeRepository.getRepairTypeList()
    }
}
