package ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.repairtype

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.catalog.RepairType
import ru.internetcloud.workorderapplication.common.domain.repository.RepairTypeRepository

class GetRepairTypeListUseCase @Inject constructor(
    private val repairTypeRepository: RepairTypeRepository
) {
    suspend fun getRepairTypeList(): List<RepairType> {
        return repairTypeRepository.getRepairTypeList()
    }
}
