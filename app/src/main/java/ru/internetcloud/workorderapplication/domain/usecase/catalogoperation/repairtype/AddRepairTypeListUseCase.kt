package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbRepairTypeRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository
import javax.inject.Inject

class AddRepairTypeListUseCase @Inject constructor(
    @DbRepairTypeRepositoryQualifier private val repairTypeRepository: RepairTypeRepository
) {

    suspend fun addRepairTypeList(repairTypeList: List<RepairType>) {
        return repairTypeRepository.addRepairTypeList(repairTypeList)
    }
}
