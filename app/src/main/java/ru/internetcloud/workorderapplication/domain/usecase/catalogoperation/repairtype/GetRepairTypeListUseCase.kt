package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype

import ru.internetcloud.workorderapplication.domain.model.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository
import javax.inject.Inject

class GetRepairTypeListUseCase @Inject constructor(
    private val repairTypeRepository: RepairTypeRepository
) {
    suspend fun getRepairTypeList(): List<RepairType> {
        return repairTypeRepository.getRepairTypeList()
    }
}
